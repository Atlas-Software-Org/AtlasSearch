package de.glowman554.search.utils.config;

import de.glowman554.config.auto.JsonProcessor;
import net.shadew.json.JsonNode;

import java.util.Date;

public class DateProcessor implements JsonProcessor {
    @Override
    public JsonNode toJson(Object o) {
        return JsonNode.number(((Date) o).getTime());
    }

    @Override
    public Object fromJson(JsonNode jsonNode, Object o) {
        return jsonNode != null ? new Date(jsonNode.asLong()) : o;
    }
}
