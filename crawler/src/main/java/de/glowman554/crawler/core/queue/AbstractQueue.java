package de.glowman554.crawler.core.queue;

import de.glowman554.crawler.core.IPageInserter;
import de.glowman554.crawler.core.queue.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractQueue {
    private final IPageInserter pageInserter;

    public AbstractQueue(IPageInserter pageInserter) {

        this.pageInserter = pageInserter;
    }

    protected abstract void push(@NotNull String url);

    public abstract @Nullable String pop();

    public abstract void clearWithPrefix(String prefix);

    public void insert(@NotNull String url) {
        String newUrl = Validator.validate(url);
        if (newUrl != null) {
            if (pageInserter.test(newUrl)) {
                return;
            }
            push(newUrl);
        }
    }

}
