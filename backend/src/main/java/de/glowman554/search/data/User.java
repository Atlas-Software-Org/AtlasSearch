package de.glowman554.search.data;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class User extends AutoSavable {
    @Saved
    private String username;
    @Saved
    private String profilePictureUrl;
    @Saved
    private boolean isAdministrator;
    @Saved
    private boolean isPremiumUser;
    @Saved(remap = Savable.class)
    private UserConfiguration configuration = new UserConfiguration();

    private String passwordHash;

    public User() {
    }

    public User(String username, String profilePictureUrl, String passwordHash, boolean isAdministrator,
                boolean isPremiumUser, UserConfiguration configuration) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.passwordHash = passwordHash;
        this.isAdministrator = isAdministrator;
        this.isPremiumUser = isPremiumUser;
        this.configuration = configuration;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public UserConfiguration getConfiguration() {
        return configuration;
    }
}
