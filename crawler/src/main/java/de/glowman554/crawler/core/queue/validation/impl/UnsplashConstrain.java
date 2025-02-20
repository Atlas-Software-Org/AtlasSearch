package de.glowman554.crawler.core.queue.validation.impl;


import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

public class UnsplashConstrain implements Constrain<String> {

    @Override
    public boolean test(@NotNull String input) {
        return input.contains("unsplash") && input.contains("download");
    }

}