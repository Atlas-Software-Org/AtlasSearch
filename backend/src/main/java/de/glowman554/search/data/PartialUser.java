package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class PartialUser extends AutoSavable {
    @Saved
    private String username;
    @Saved
    private String profilePictureUrl;

    public PartialUser() {
    }

    public PartialUser(String username, String profilePictureUrl) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
