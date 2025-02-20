package de.glowman554.search.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class HttpClient {
    private static final String proxyPort = System.getenv("PROXY_PORT");
    private static final String proxyHost = System.getenv("PROXY_HOST");

    private static final String userAgent = "YunaBot";

    public static String get(String url, Map<String, String> headers, boolean proxy) throws IOException, RateLimitException {

        OkHttpClient client;
        if (proxy) {
            InetSocketAddress address = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort));
            client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.SOCKS, address)).build();
        } else {
            client = new OkHttpClient();
        }

        var req = new Request.Builder();

        req.url(url);

        req.addHeader("User-Agent", userAgent);

        for (String key : headers.keySet()) {
            req.addHeader(key, headers.get(key));
        }

        try (Response res = client.newCall(req.build()).execute()) {
            if (!res.isSuccessful()) {
                if (res.code() == 429) {
                    URL parsedUrl = URI.create(url).toURL();
                    throw new RateLimitException(parsedUrl.getProtocol() + "://" + parsedUrl.getHost());
                }
                throw new IOException("HTTP " + res.code() + " (" + url + ")");
            }

            assert res.body() != null;
            return res.body().string();
        }

    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static class RateLimitException extends Exception {
        private final String urlPrefix;

        public RateLimitException(String urlPrefix) {
            super("Rate limit reached for " + urlPrefix);
            this.urlPrefix = urlPrefix;
        }

        public String getUrlPrefix() {
            return urlPrefix;
        }
    }
}
