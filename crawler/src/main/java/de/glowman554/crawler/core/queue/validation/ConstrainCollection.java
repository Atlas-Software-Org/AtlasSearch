package de.glowman554.crawler.core.queue.validation;

import java.util.ArrayList;

public class ConstrainCollection<In> {
    private final ArrayList<Constrain<In>> steps = new ArrayList<>();

    public boolean test(In input) {
        for (Constrain<In> step : steps) {
            if (step.test(input)) {
                return true;
            }
        }

        return false;
    }

    public void add(Constrain<In> c) {
        steps.add(c);
    }
}