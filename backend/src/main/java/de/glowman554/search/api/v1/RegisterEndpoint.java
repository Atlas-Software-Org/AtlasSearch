package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.JsonHandler;
import de.glowman554.search.data.UserAndPassword;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class RegisterEndpoint extends JsonHandler<UserAndPassword> {

    @Override
    public UserAndPassword createInstance() {
        return new UserAndPassword();
    }

    @Override
    public void handle(@NotNull Context context, UserAndPassword instance) throws Exception {
        if (instance.getUsername().isEmpty() || instance.getPassword().isEmpty()) {
            context.status(400);
            context.result("Bad request");
            return;
        }

        String token = Main.getUserManager().registerAndCreateSession(instance.getUsername(), instance.getPassword());
        context.cookie("Authentication", token);

        json(context, JsonNode.object().set("status", "success"));
    }
}
