package de.glowman554.crawler.core.queue.validation.impl;

import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

public class WiktionaryConstrain implements Constrain<String> {

    @Override
    public boolean test(@NotNull String input) {
        if (input.contains("wiktionary.org")) {
            return !(input.contains("de.wiktionary.org") || input.contains("en.wiktionary.org"));
        }

        return false;
    }

}