package de.glowman554.crawler.core.queue.impl;

import de.glowman554.crawler.core.IPageInserter;
import de.glowman554.crawler.core.queue.AbstractQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class MemoryQueue extends AbstractQueue {
    private final Stack<String> stack = new Stack<>();

    public MemoryQueue(IPageInserter pageInserter) {
        super(pageInserter);
    }

    @Override
    protected void push(@NotNull String url) {
        if (stack.contains(url) || stack.size() > 10000) {
            return;
        }
        stack.push(url);
    }

    @Nullable
    @Override
    public String pop() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    @Override
    public void clearWithPrefix(String prefix) {
        stack.removeIf(s -> s.startsWith(prefix));
    }
}
