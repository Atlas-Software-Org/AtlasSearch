package de.glowman554.search.data;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.Saved;

public class User extends PartialUser {

    @Saved
    private boolean isAdministrator;
    @Saved
    private boolean isPremiumUser;
    @Saved(remap = Savable.class)
    private UserConfiguration configuration = new UserConfiguration();

    private String passwordHash;

    public User() {
        super();
    }

    public User(String username, String profilePictureUrl, String passwordHash, boolean isAdministrator,
                boolean isPremiumUser, UserConfiguration configuration) {
        super(username, profilePictureUrl);
        this.passwordHash = passwordHash;
        this.isAdministrator = isAdministrator;
        this.isPremiumUser = isPremiumUser;
        this.configuration = configuration;
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
