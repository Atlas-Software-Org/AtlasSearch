package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class TimingAverage extends AutoSavable {
    @Saved
    private String date;
    @Saved
    private int timing;

    public TimingAverage(String date, int timing) {
        this.date = date;
        this.timing = timing;
    }

    public String getDate() {
        return date;
    }

    public int getTiming() {
        return timing;
    }
}
