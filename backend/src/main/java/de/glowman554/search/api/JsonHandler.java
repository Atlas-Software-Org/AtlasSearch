package de.glowman554.search.api;

import de.glowman554.config.Savable;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public abstract class JsonHandler<T extends Savable> extends BaseHandler {

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String body = context.body();

        JsonNode root;
        try {
            root = json.parse(body);
        } catch (Exception e) {
            context.status(400);
            context.result("Bad request");
            return;
        }

        T instance = createInstance();
        instance.fromJSON(root);

        handle(context, instance);
    }

    public abstract T createInstance();

    public abstract void handle(@NotNull Context context, T instance) throws Exception;
}