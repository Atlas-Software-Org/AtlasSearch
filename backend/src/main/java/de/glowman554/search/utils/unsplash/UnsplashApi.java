package de.glowman554.search.utils.unsplash;

import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Objects;

public class UnsplashApi {
    private final String unsplashToken;
    private final OkHttpClient client = new OkHttpClient();
    private final Json json = Json.json();

    public UnsplashApi(String unsplashToken) {
        this.unsplashToken = unsplashToken;
    }

    public UnsplashImageResponse requestPicture() {
        Request request = new okhttp3.Request.Builder()
                .url("https://api.unsplash.com/photos/random?orientation=landscape&content_filter=high&topics=nature,wallpapers")
                .header("Authorization", "Client-ID " + unsplashToken)
                .header("Accept", "application/json")
                .build();

        try (Response res = client.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("Failed to prepare upload");
            }

            JsonNode parsed = json.parse(Objects.requireNonNull(res.body()).string());
            UnsplashImageResponse result = new UnsplashImageResponse();
            result.fromJSON(parsed);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare upload", e);
        }
    }
}
