package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

import java.util.Date;

public class CrawlHistoryEntry extends AutoSavable {
    @Saved
    private String username;
    @Saved
    private int id;
    @Saved
    private String url;
    @Saved
    private Date timestamp;
    @Saved
    private String status;

    public CrawlHistoryEntry(String username, int id, String url, Date timestamp, String status) {
        this.username = username;
        this.id = id;
        this.url = url;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
