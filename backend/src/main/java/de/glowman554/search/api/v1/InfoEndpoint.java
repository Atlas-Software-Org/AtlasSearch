package de.glowman554.search.api.v1;

import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class InfoEndpoint extends BaseHandler {

    @Override
    public void handle(@NotNull Context context) throws Exception {
        User user = authenticated(context, true);
        json(context, user);
    }
}
