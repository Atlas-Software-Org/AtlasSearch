package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class Timing extends AutoSavable {
    @Saved
    private String key;
    @Saved
    private int time;

    public String getKey() {
        return key;
    }

    public int getTime() {
        return time;
    }
}
