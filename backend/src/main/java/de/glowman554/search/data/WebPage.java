package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

import java.util.Date;

public class WebPage extends AutoSavable {
    @Saved
    private String link;
    @Saved
    private String title;
    @Saved
    private String content;
    @Saved
    private String description;
    @Saved
    private String keywords;
    @Saved
    private String shortText;
    @Saved
    private Date timestamp;

    public WebPage(String link, String title, String content, String description, String keywords, String shortText, Date timestamp) {
        this.link = link;
        this.title = title;
        this.content = content;
        this.description = description;
        this.keywords = keywords;
        this.shortText = shortText;
        this.timestamp = timestamp;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getShortText() {
        return shortText;
    }

    public Date getTimestamp() {
        return timestamp;
    }


}
