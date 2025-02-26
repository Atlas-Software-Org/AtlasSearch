package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class UserEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        administrator(ctx);

        String username = ctx.queryParam("u");
        if (username == null || username.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing query parameter");
            return;
        }

        User user = Main.getDatabase().users.loadUser(username);
        if (user == null) {
            throw new Exception("User not found");
        }

        json(ctx, user);
    }
}
