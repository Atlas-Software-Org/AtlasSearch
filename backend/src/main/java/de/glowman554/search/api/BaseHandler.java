package de.glowman554.search.api;

import de.glowman554.config.Savable;
import de.glowman554.search.Main;
import de.glowman554.search.data.User;
import de.glowman554.search.utils.MutedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

public abstract class BaseHandler implements Handler {
    protected Json json = Json.json();

    public void json(Context context, JsonNode object) {
        context.contentType("application/json");
        context.result(json.serialize(object));
    }

    public void json(Context context, Savable object) {
        json(context, object.toJSON());
    }

    public User authenticated(Context context, boolean required) {
        String token = context.cookie("Authentication");
        if (token == null) {
            if (required) {
                throw new MutedException("Missing authentication token");
            }
            return null;
        }

        return Main.getUserManager().getSession(token, required);
    }
}
