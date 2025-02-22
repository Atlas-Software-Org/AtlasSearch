package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.utils.unsplash.UnsplashImageResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class BackgroundEndpoint extends BaseHandler {
    private String currentBackgroundUrl = "https://images.unsplash.com/photo-1544259342-306eccfec481?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80";
    private String currentUsername = "";
    private String currentName = "";
    private long lastUpdate = 0;

    public BackgroundEndpoint() {
        startUpdate();
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // Update the background image every 10
        if (System.currentTimeMillis() - lastUpdate > 1000 * 60 * 10) {
            startUpdate();
        }

        String css = ":root { --bg-url: url(\"" + currentBackgroundUrl + "\"); --bg-username: " + currentUsername + "; --bg-name: " + currentName + "; }";

        ctx.contentType("text/css");
        ctx.result(css);
    }

    private void startUpdate() {
        new Thread(() -> {
            UnsplashImageResponse response = Main.getUnsplashApi().requestPicture();

            currentBackgroundUrl = response.getUrls().getRegular();
            currentUsername = response.getUser().getUsername();
            currentName = response.getUser().getName();

            lastUpdate = System.currentTimeMillis();
        }).start();
    }
}
