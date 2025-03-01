package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class UserConfiguration extends AutoSavable {
    @Saved
    private boolean shouldKeepHistory;
    @Saved
    private boolean shouldUseAi;

    public UserConfiguration() {
    }

    public UserConfiguration(boolean shouldKeepHistory, boolean shouldUseAi) {
        this.shouldKeepHistory = shouldKeepHistory;
        this.shouldUseAi = shouldUseAi;
    }

    public boolean shouldKeepHistory() {
        return shouldKeepHistory;
    }

    public boolean shouldUseAi() {
        return shouldUseAi;
    }
}
