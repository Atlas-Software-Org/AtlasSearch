package de.glowman554.crawler.core.robots;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Scope {
    private final ArrayList<String> allows = new ArrayList<>();
    private final ArrayList<String> disallows = new ArrayList<>();
    private final String path;

    public Scope(String path) {
        this.path = path;
    }


    public ArrayList<String> getAllows() {
        return allows;
    }

    public ArrayList<String> getDisallows() {
        return disallows;
    }

    public String getPath() {
        return path;
    }

    private boolean matches(String path, String pattern) {
        int pathlen = path.length();
        int[] pos = new int[pathlen + 1];
        int numpos;

        pos[0] = 0;
        numpos = 1;

        for (int j = 0; j < pattern.length(); ++j) {
            if (pattern.charAt(j) == '$' && j == pattern.length() - 1) {
                return (pos[numpos - 1] == pathlen);
            }
            if (pattern.charAt(j) == '*') {
                numpos = pathlen - pos[0] + 1;
                for (int i = 1; i < numpos; i++) {
                    pos[i] = pos[i - 1] + 1;
                }
            } else {
                int newnumpos = 0;
                for (int i = 0; i < numpos; i++) {
                    if (pos[i] < pathlen && path.charAt(pos[i]) == pattern.charAt(j)) {
                        pos[newnumpos++] = pos[i] + 1;
                    }
                }
                numpos = newnumpos;
                if (numpos == 0) {
                    return false;
                }
            }
        }

        return true;
    }


    public boolean shouldCrawl(String fullUrl) throws MalformedURLException {
        URL url = URI.create(fullUrl).toURL();

        String path = url.getPath();

        for (String allow : allows) {
            if (matches(path, allow)) {
                return true;
            }
        }

        for (String disallow : disallows) {
            if (matches(path, disallow)) {
                return false;
            }
        }

        return true;
    }
}