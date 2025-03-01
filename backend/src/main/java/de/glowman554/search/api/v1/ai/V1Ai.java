package de.glowman554.search.api.v1.ai;

import io.javalin.Javalin;

public class V1Ai {
    public static void register(Javalin app) {
        app.get("/api/v1/ai/answer", new AnswerEndpoint());
    }
}
