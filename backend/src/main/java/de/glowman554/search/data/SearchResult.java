package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class SearchResult extends AutoSavable {
    @Saved
    private String link;
    @Saved
    private String title;
    @Saved
    private String description;
    @Saved
    private String shortText;
    @Saved
    public double score;

    public SearchResult(String link, String title, String description, String shortText, double score) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.shortText = shortText;
        this.score = score;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortText() {
        return shortText;
    }

    public double getScore() {
        return score;
    }
}
