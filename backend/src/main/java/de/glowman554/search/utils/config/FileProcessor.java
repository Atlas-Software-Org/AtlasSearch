package de.glowman554.search.utils.config;

import de.glowman554.config.auto.JsonProcessor;
import net.shadew.json.JsonNode;

import java.io.File;

public class FileProcessor implements JsonProcessor {
    @Override
    public JsonNode toJson(Object o) {
        return JsonNode.string(((File) o).getPath());
    }

    @Override
    public Object fromJson(JsonNode jsonNode, Object o) {
        return jsonNode != null ? new File(jsonNode.asString()) : o;
    }
}
