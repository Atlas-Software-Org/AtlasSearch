package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.PartialUser;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;


public class UserListEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        administrator(ctx);

        JsonNode response = JsonNode.array();
        for (PartialUser user : Main.getDatabase().users.loadUserList()) {
            response.add(user.toJSON());
        }

        json(ctx, response);
    }
}
