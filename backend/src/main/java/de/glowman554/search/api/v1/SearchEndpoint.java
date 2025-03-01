package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.SearchResult;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String query = ctx.queryParam("q");
        if (query == null || query.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing query parameter");
            return;
        }

        User user = authenticated(ctx, false);
        if (user != null) {
            if (user.getConfiguration().shouldKeepHistory()) {
                Main.getDatabase().history.insertSearchHistory(user.getUsername(), query);
            }
        }

        String pageStr = ctx.queryParam("page");
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "0";
        }

        int page = Integer.parseInt(pageStr);

        ArrayList<SearchResult> results = Main.getDatabase().performSearch(query, page);
        JsonNode result = JsonNode.array();
        for (SearchResult searchResult : results) {
            result.add(searchResult.toJSON());
        }

        json(ctx, result);
    }
}
