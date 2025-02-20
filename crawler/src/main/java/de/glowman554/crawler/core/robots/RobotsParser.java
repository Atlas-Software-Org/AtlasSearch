package de.glowman554.crawler.core.robots;

import de.glowman554.search.utils.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RobotsParser {
    public static Result parse(String robots, String host) {
        ArrayList<String> sitemaps = new ArrayList<>();
        HashMap<String, Scope> scopes = new HashMap<>();
        String currentScope = "*";

        for (String entry : robots.split("\\n")) {
            entry = entry.trim();
            if (entry.isEmpty()) {
                continue;
            }

            String[] split = entry.split(":", 2);
            String instruction = split[0].toLowerCase();
            switch (instruction) {
                case "sitemap":
                    sitemaps.add(split[1].trim());
                    break;

                case "user-agent":
                    currentScope = split[1].trim();
                    break;

                case "disallow":
                case "allow":
                    if (!scopes.containsKey(currentScope)) {
                        scopes.put(currentScope, new Scope(currentScope));
                    }
                    Scope scope = scopes.get(currentScope);

                    if (instruction.equals("allow")) {
                        scope.getAllows().add(split[1].trim());
                    } else {
                        scope.getDisallows().add(split[1].trim());
                    }
                    break;
                default:
                    if (instruction.startsWith("#")) {
                        break;
                    }
                    Logger.log("WARNING[" + host + "]: unknown robots.txt instruction " + instruction);
                    break;
            }
        }

        return new Result(sitemaps, scopes);
    }

    public static String link(URL url) {
        String finalLink = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() != -1) {
            finalLink += ":" + url.getPort();
        }
        finalLink += "/robots.txt";
        return finalLink;
    }
}