package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.JsonHandler;
import de.glowman554.search.data.ChangePasswordRequest;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class ChangePasswordEndpoint extends JsonHandler<ChangePasswordRequest> {
    @Override
    public ChangePasswordRequest createInstance() {
        return new ChangePasswordRequest();
    }

    @Override
    public void handle(@NotNull Context context, ChangePasswordRequest instance) throws Exception {
        User user = authenticated(context, true);

        if (instance.getNewPassword() == null || instance.getNewPassword().isEmpty() || instance.getOldPassword() == null || instance.getOldPassword().isEmpty()) {
            context.status(400);
            context.result("Bad request - missing old or new password");
            return;
        }

        Main.getUserManager().changePasswordAndDeleteSessions(user, instance.getOldPassword(), instance.getNewPassword());

        json(context, JsonNode.object().set("status", "success"));
    }
}
