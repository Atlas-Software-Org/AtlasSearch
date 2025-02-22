package de.glowman554.search.api.v1.admin;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.TimingAverage;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TimingAverageEndpoint extends BaseHandler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        administrator(ctx);

        HashMap<String, ArrayList<TimingAverage>> response = Main.getDatabaseConnection().loadTimingAverages();

        JsonNode result = JsonNode.object();
        for (String key : response.keySet()) {
            ArrayList<TimingAverage> latencies = response.get(key);
            result.set(key, JsonNode.array(latencies.stream().map(TimingAverage::toJSON).toList()));
        }

        json(ctx, result);
    }
}
