package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class UserConfiguration extends AutoSavable {
    @Saved
    private boolean shouldKeepHistory;

    public UserConfiguration() {
    }

    public UserConfiguration(boolean shouldKeepHistory) {
        this.shouldKeepHistory = shouldKeepHistory;
    }

    public boolean shouldKeepHistory() {
        return shouldKeepHistory;
    }
}
