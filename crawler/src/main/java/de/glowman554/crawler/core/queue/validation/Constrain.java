package de.glowman554.crawler.core.queue.validation;

import org.jetbrains.annotations.NotNull;

public interface Constrain<In> {
    boolean test(@NotNull In input);
}