package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.JsonHandler;
import de.glowman554.search.data.UserMetadata;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class ChangeUserMetadataEndpoint extends JsonHandler<UserMetadata> {
    @Override
    public UserMetadata createInstance() {
        return new UserMetadata();
    }

    @Override
    public void handle(@NotNull Context context, UserMetadata instance) throws Exception {
        administrator(context);

        Main.getDatabase().users.updateUserMetadata(instance);

        json(context, JsonNode.object().set("success", true));
    }
}
