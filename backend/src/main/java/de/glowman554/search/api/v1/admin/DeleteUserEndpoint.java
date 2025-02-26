package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class DeleteUserEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        administrator(ctx);

        String username = ctx.queryParam("u");
        if (username == null || username.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing query parameter");
            return;
        }

        Main.getDatabase().users.deleteUser(username);

        json(ctx, JsonNode.object().set("success", true));
    }
}
