package de.glowman554.search.api.v1;

import de.glowman554.search.Main;
import de.glowman554.search.api.JsonHandler;
import de.glowman554.search.data.Timing;
import de.glowman554.search.data.User;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class TimingEndpoint extends JsonHandler<Timing> {
    @Override
    public Timing createInstance() {
        return new Timing();
    }

    @Override
    public void handle(@NotNull Context context, Timing instance) throws Exception {
        User user = authenticated(context, false);
        if (user == null) {
            Main.getDatabase().insertTimingEvent(null, instance.getKey(), instance.getTime());
        } else {
            Main.getDatabase().insertTimingEvent(user.getUsername(), instance.getKey(), instance.getTime());
        }
        json(context, JsonNode.object().set("status", "success"));
    }
}
