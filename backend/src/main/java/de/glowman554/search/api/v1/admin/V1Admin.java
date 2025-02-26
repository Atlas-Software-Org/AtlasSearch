package de.glowman554.search.api.v1.admin;

import io.javalin.Javalin;

public class V1Admin {
    public static void register(Javalin app) {
        app.get("/api/v1/admin/timingAverage", new TimingAverageEndpoint());
        app.get("/api/v1/admin/crawlHistory", new CrawlHistoryEndpoint());
        app.get("/api/v1/admin/user", new UserEndpoint());
        app.get("/api/v1/admin/userList", new UserListEndpoint());
        app.post("/api/v1/admin/changeUserMetadata", new ChangeUserMetadataEndpoint());
        app.get("/api/v1/admin/deleteUser", new DeleteUserEndpoint());
    }
}
