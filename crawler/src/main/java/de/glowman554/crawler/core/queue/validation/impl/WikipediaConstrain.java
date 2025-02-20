package de.glowman554.crawler.core.queue.validation.impl;


import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

public class WikipediaConstrain implements Constrain<String> {

    @Override
    public boolean test(@NotNull String input) {
        if (input.contains("wikipedia.org")) {
            return !(input.contains("de.wikipedia.org") || input.contains("en.wikipedia.org"));
        }

        return false;
    }

}