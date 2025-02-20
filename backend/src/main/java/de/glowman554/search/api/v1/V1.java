package de.glowman554.search.api.v1;

import io.javalin.Javalin;

public class V1 {
    public static void register(Javalin app) {
        app.post("/api/v1/login", new LoginEndpoint());
        app.post("/api/v1/register", new RegisterEndpoint());
        app.get("/api/v1/info", new InfoEndpoint());

        app.get("/api/v1/search", new SearchEndpoint());
    }
}
