package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.CrawlHistoryEntry;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CrawlHistoryEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        administrator(ctx);

        ArrayList<CrawlHistoryEntry> response = Main.getDatabase().history.loadCrawlHistory();
        JsonNode result = JsonNode.array();
        for (CrawlHistoryEntry entry : response) {
            result.add(entry.toJSON());
        }

        json(ctx, result);
    }
}
