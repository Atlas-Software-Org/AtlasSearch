package de.glowman554.search.api.v1;

import de.glowman554.crawler.core.Crawler;
import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class CrawlEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        User user = authenticated(ctx, true);
        String link = ctx.queryParam("l");

        if (link == null || link.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing link parameter");
            return;
        }

        Crawler.CrawlerStatus status = Main.getCrawler().validatedPerform(link);
        Main.getDatabaseConnection().insertCrawlRequest(user.getUsername(), link, status);


        json(ctx, JsonNode.object().set("status", status.name()));
    }
}
