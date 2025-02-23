package de.glowman554.crawler.core;

import de.glowman554.crawler.core.analysis.KeywordAnalysis;
import de.glowman554.crawler.core.queue.AbstractQueue;
import de.glowman554.crawler.core.queue.validation.Validator;
import de.glowman554.crawler.core.robots.Result;
import de.glowman554.crawler.core.robots.RobotsParser;
import de.glowman554.crawler.core.robots.Scope;
import de.glowman554.crawler.utils.ThreadPool;
import de.glowman554.search.utils.HttpClient;
import de.glowman554.search.utils.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private final ThreadPool pool = new ThreadPool(4);
    private final AbstractQueue queue;
    private final IPageInserter inserter;
    private final HashMap<String, Result> robotsCache = new HashMap<>();
    private boolean running = true;

    public Crawler(AbstractQueue queue, IPageInserter inserter) {
        this.queue = queue;
        this.inserter = inserter;
    }

    public void startLoop() {
        Thread updateThread = new Thread(this::updateLoop);
        updateThread.start();

        while (running) {
            String link = queue.pop();
            if (link == null) {
                ThreadPool.safeSleep(100);
                continue;
            }

            pool.submit(() -> perform(link));
        }
        pool.stop();
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLoop() {
        while (running) {
            String oldest = inserter.fetchOldest();
            if (oldest == null) {
                ThreadPool.safeSleep(100);
                continue;
            }
            perform(oldest);
        }
    }

    public CrawlerStatus perform(String link) {
        try {
            CrawlerStatus status = crawl(link);
            Logger.log("[CRAWLER] " + status + " " + link);
            return status;
        } catch (Exception e) {
            Logger.exception(e);
            inserter.delete(link);
        }
        return CrawlerStatus.FAILED;
    }

    public CrawlerStatus validatedPerform(String link) {
        String s = Validator.validate(link);
        if (s == null) {
            return CrawlerStatus.REJECTED;
        }

        return perform(s);
    }

    public void stop() {
        running = false;
    }

    private String fetch(String link) throws IOException, HttpClient.RateLimitException {
        boolean proxy = link.contains(".onion") || "true".equals(System.getenv("PROXY_FORCE"));
        return HttpClient.get(link, Map.of(), proxy);
    }

    private boolean processSitemap(String sitemapUrl) {
        try {
            String sitemap = fetch(sitemapUrl);
            Document jsoup = Jsoup.parse(sitemap);

            int discoveries = 0;

            for (Element url : jsoup.getElementsByTag("url")) {
                Elements child = url.getElementsByTag("loc");
                if (child.size() == 1) {
                    String site = child.getFirst().text().trim().split("#")[0];
                    queue.insert(site);
                    discoveries++;
                }
            }

            Logger.log("[SITEMAP] discovered " + discoveries + " links in " + sitemapUrl);
        } catch (Exception e) {
            Logger.exception(e);
            return true;
        }
        return false;
    }

    private CrawlerStatus crawl(String link) throws IOException {
        if (robotsCache.size() > 500) {
            robotsCache.clear();
        }

        URL url = URI.create(link).toURL();
        Result robots = null;
        String robotsCacheKey = url.getHost() + ":" + url.getPort();
        if (robotsCache.containsKey(robotsCacheKey)) {
            robots = robotsCache.get(robotsCacheKey);
        } else {
            try {
                String robotsString = fetch(RobotsParser.link(url));
                robots = RobotsParser.parse(robotsString, url.getHost());
            } catch (IOException | HttpClient.RateLimitException ignored) {
            }
            robotsCache.put(robotsCacheKey, robots);

            if (robots != null) {
                for (String sitemap : robots.sitemaps()) {
                    if (processSitemap(sitemap)) {
                        robots.invalidateSitemaps();
                        break;
                    }
                }
            }
        }

        if (robots != null) {
            Scope scope = robots.getApplyingScope(HttpClient.getUserAgent());
            if (scope != null) {
                if (!scope.shouldCrawl(link)) {
                    return CrawlerStatus.REJECTED;
                }
            }
        }

        Document doc;
        try {
            doc = Jsoup.parse(fetch(link), link);
        } catch (HttpClient.RateLimitException e) {
            Logger.exception(e);
            queue.clearWithPrefix(e.getUrlPrefix());
            return CrawlerStatus.FAILED;
        }

        if (!(robots != null && !robots.sitemaps().isEmpty())) {
            Elements links = doc.getElementsByTag("a");

            if (queue != null) {
                for (Element element : links) {
                    String href = element.absUrl("href");
                    queue.insert(href.split("#")[0]);
                }
            }
        }

        Elements titles = doc.getElementsByTag("title");
        String title = "none";
        if (!titles.isEmpty()) {
            title = titles.getFirst().text();
        }


        String description = null;
        String keywords = null;
        Elements metas = doc.getElementsByTag("meta");
        for (Element meta : metas) {
            if (meta.attr("name").equals("description") || meta.attr("property").equals("og:description")) {
                description = meta.attr("content");
            } else if (meta.attr("name").equals("keywords")) {
                keywords = meta.attr("content");
            }
        }

        StringBuilder shortText = new StringBuilder();
        Elements shortTextElements = doc.getElementsByTag("article");
        if (shortTextElements.isEmpty()) {
            shortTextElements = doc.getElementsByTag("p");
        }
        for (Element shortTextElement : shortTextElements) {
            shortText.append(shortTextElement.text()).append(" ");
            if (shortText.length() > 175) {
                break;
            }
        }

        shortText = new StringBuilder(shortText.substring(0, Math.min(shortText.length(), 175)) + "...");

        String text = doc.text();

        if (keywords == null) {
            keywords = String.join(", ", KeywordAnalysis.analyseKeyword(text, 5));
            // Logger.log("[KEYWORDS] " + link + " " + keywords);
        }

        if (inserter.test(link)) {
            inserter.update(link, title, text, description, keywords, shortText.toString());
            return CrawlerStatus.UPDATED;
        } else {
            inserter.insert(link, title, text, description, keywords, shortText.toString());
            return CrawlerStatus.INSERTED;
        }
    }

    public enum CrawlerStatus {
        INSERTED, UPDATED, REJECTED, FAILED
    }
}
