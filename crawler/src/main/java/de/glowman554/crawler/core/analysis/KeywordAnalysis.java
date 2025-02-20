package de.glowman554.crawler.core.analysis;

import java.util.*;

public class KeywordAnalysis {
    private static final HashSet<String> invalidKeywords = new HashSet<>() {{
        add("issues");
        add("pulls");
        add("updated");
        add("requests");
        add("actions");
        add("github");
        add("search");
        add("delete");
        add("create");
    }};

    public static List<String> analyseKeyword(String text, int count) {
        HashMap<String, Integer> keywords = new HashMap<>();

        text = text.replace("\n", " ");
        text = text.replace("\r", " ");
        text = text.replace("\t", " ");

        Arrays.stream(text.split(" "))
                .map(String::toLowerCase)
                .filter(s -> s.length() > 4)
                .filter(s -> !invalidKeywords.contains(s))
                .forEach(s -> keywords.put(s, keywords.getOrDefault(s, 0) + 1));

        return keywords.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .filter(e -> e.getValue() > 1)
                .limit(count)
                .map(Map.Entry::getKey).toList();
    }
}
