package de.glowman554.crawler.core.robots;

import java.util.ArrayList;
import java.util.HashMap;

public record Result(ArrayList<String> sitemaps, HashMap<String, Scope> scopes) {

    public Scope getApplyingScope(String useragent) {
        Scope matchingScope = null;
        Scope defaultScope = null;

        for (String agent : scopes.keySet()) {
            if (useragent.toLowerCase().contains(agent.toLowerCase())) {
                Scope scope = scopes.get(agent);

                if (matchingScope == null || scope.getPath().length() > matchingScope.getPath().length()) {
                    matchingScope = scope;
                }
            }

            if (agent.equals("*")) {
                defaultScope = scopes.get(agent);
            }
        }

        return matchingScope == null ? defaultScope : matchingScope;
    }

    public void invalidateSitemaps() {
        sitemaps.clear();
    }
}