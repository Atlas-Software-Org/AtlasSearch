package de.glowman554.search.api.v1;

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
    }
}
