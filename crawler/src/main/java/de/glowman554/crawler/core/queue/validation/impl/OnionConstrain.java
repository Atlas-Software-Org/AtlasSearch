package de.glowman554.crawler.core.queue.validation.impl;

import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

public class OnionConstrain implements Constrain<String> {
    private final boolean allow = "true".equals(System.getenv("ALLOW_ONION"));

    @Override
    public boolean test(@NotNull String input) {
        if (allow) {
            return false;
        } else {
            return input.contains(".onion");
        }
    }
}