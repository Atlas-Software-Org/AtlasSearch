package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class UserMetadata extends AutoSavable {
    @Saved
    private String username;
    @Saved
    private boolean isAdministrator;
    @Saved
    private boolean isPremiumUser;

    public String getUsername() {
        return username;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }
}
