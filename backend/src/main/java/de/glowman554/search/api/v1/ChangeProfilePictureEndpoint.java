package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.JsonHandler;
import de.glowman554.search.data.ChangeProfilePictureRequest;
import de.glowman554.search.data.User;
import de.glowman554.search.utils.filething.UploadResult;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class ChangeProfilePictureEndpoint extends JsonHandler<ChangeProfilePictureRequest> {
    @Override
    public ChangeProfilePictureRequest createInstance() {
        return new ChangeProfilePictureRequest();
    }

    @Override
    public void handle(@NotNull Context context, ChangeProfilePictureRequest instance) throws Exception {
        User user = authenticated(context, true);

        UploadResult upload = Main.getFileThingApi().prepareUpload(instance.getFileName());
        Main.getDatabase().users.updateUserProfilePicture(user.getUsername(), upload.getUrl());

        String oldId = Main.getFileThingApi().idFromUrl(user.getProfilePictureUrl());
        if (oldId != null) {
            Main.getFileThingApi().deleteFile(oldId);
        }

        json(context, upload);
    }
}
