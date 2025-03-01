package de.glowman554.search.api.v1;

import de.glowman554.search.api.v1.admin.V1Admin;
import de.glowman554.search.api.v1.ai.V1Ai;
import io.javalin.Javalin;

public class V1 {
    public static void register(Javalin app) {
        app.post("/api/v1/login", new LoginEndpoint());
        app.post("/api/v1/register", new RegisterEndpoint());
        app.get("/api/v1/info", new InfoEndpoint());
        app.post("/api/v1/changeProfilePicture", new ChangeProfilePictureEndpoint());
        app.post("/api/v1/changePassword", new ChangePasswordEndpoint());

        app.get("/api/v1/search", new SearchEndpoint());
        app.post("/api/v1/timing", new TimingEndpoint());
        app.get("/api/v1/redirect", new RedirectEndpoint());
        app.get("/api/v1/background", new BackgroundEndpoint());
        app.get("/api/v1/crawl", new CrawlEndpoint());

        V1Admin.register(app);
        V1Ai.register(app);
    }
}
