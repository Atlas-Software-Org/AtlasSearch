package de.glowman554.crawler.core.queue.validation.impl;

import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

public class WikisourceConstrain implements Constrain<String> {

    @Override
    public boolean test(@NotNull String input) {
        if (input.contains("wikisource.org")) {
            return !(input.contains("de.wikisource.org") || input.contains("en.wikisource.org"));
        }

        return false;
    }

}