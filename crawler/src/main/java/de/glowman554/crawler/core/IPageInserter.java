package de.glowman554.crawler.core;

public interface IPageInserter {
    void insert(String url, String title, String text, String description, String keywords, String shortText);

    void update(String url, String title, String content, String description, String keywords, String shortText);

    void delete(String url);

    boolean test(String url);

    String fetchOldest();
}
