package de.glowman554.crawler.core.queue.validation.impl;

import de.glowman554.crawler.core.queue.validation.Constrain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GithubOnlyMainRepo implements Constrain<String> {

    private final ArrayList<String> disallowed = new ArrayList<>();

    public GithubOnlyMainRepo() {
        disallowed.add("/fork");
        disallowed.add("/forks");
        disallowed.add("/stargazers");
        disallowed.add("/activity");
        disallowed.add("/issues");
        disallowed.add("/pulls");
        disallowed.add("/actions");
        disallowed.add("/projects");
        disallowed.add("/security");
        disallowed.add("/pulse");
        disallowed.add("/branches");
        disallowed.add("/{{ urlEncodedRefName }}");
        disallowed.add("/tags");
        disallowed.add("/watchers");
        disallowed.add("/releases");
        disallowed.add("/search");
        disallowed.add("/contributors");
    }

    @Override
    public boolean test(@NotNull String input) {
        if (input.startsWith("https://github.com")) {
            for (String tmp : disallowed) {
                if (input.endsWith(tmp)) {
                    return true;
                }
            }
        }
        return false;
    }

}