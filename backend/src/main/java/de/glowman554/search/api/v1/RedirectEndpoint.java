package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.User;
import de.glowman554.search.data.UserConfiguration;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class RedirectEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String link = ctx.queryParam("l");
        if (link == null || link.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing link parameter");
            return;
        }

        User user = authenticated(ctx, false);
        if (user != null) {
            UserConfiguration userConfiguration = Main.getDatabaseConnection().loadUserConfiguration(user.getUsername());
            if (userConfiguration.shouldKeepHistory()) {
                Main.getDatabaseConnection().insertVisitHistory(user.getUsername(), link);
            }
        }


        ctx.redirect(link);
    }
}
