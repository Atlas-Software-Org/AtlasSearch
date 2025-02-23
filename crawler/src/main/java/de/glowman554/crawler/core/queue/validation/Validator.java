package de.glowman554.crawler.core.queue.validation;


import de.glowman554.crawler.core.queue.validation.impl.*;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Validator {
    private static final ArrayList<String> allowedExtensions = new ArrayList<>();
    private static final ConstrainCollection<String> validator = new ConstrainCollection<>();

    static {
        allowedExtensions.add("html");
        allowedExtensions.add("php");
    }

    static {
        validator.add(new WikipediaConstrain());
        validator.add(new WiktionaryConstrain());
        validator.add(new WikisourceConstrain());
        validator.add(new GithubOnlyMainRepo());
        validator.add(new GithubUserContentConstrain());
        validator.add(new UnsplashConstrain());
        validator.add(new OnionConstrain());
    }


    private static String getFileExtension(String path) {
        int i = path.lastIndexOf('.');
        if (i > 0) {
            return path.substring(i + 1);
        }
        return "";
    }

    private static String normalizeLink(String link) throws MalformedURLException {
        URL url = URI.create(link).toURL();

        if (!(allowedExtensions.contains(getFileExtension(url.getPath())) || getFileExtension(url.getPath()).isEmpty())) {
            return null;
        }

        String finalLink = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() != -1) {
            finalLink += ":" + url.getPort();
        }
        finalLink += url.getPath();
        if (finalLink.endsWith("/")) {
            finalLink = finalLink.substring(0, finalLink.length() - 1);
        }

        return finalLink;
    }

    public static @Nullable String validate(String link) {
        if (link.isEmpty() || !(link.startsWith("https://") || link.startsWith("http://"))) {
            // Logger.log("Dropping: " + link);
            return null;
        }

        if (validator.test(link)) {
            // Logger.log("Dropping: " + link);
            return null;
        }

        try {
            return normalizeLink(link);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}