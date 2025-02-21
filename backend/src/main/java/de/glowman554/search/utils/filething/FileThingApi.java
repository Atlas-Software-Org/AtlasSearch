package de.glowman554.search.utils.filething;

import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Objects;

public class FileThingApi {
    private final OkHttpClient client = new OkHttpClient();
    private final String uploadServer;
    private final String authToken;
    private final Json json = Json.json();

    public FileThingApi(String uploadServer, String authToken) {
        this.uploadServer = uploadServer;
        this.authToken = authToken;
    }

    public UploadResult prepareUpload(String fileName) {
        Request request = new Request.Builder()
                .url(uploadServer + "/api/v1/prepare")
                .post(RequestBody.create(json.serialize(JsonNode.object().set("name", fileName)), okhttp3.MediaType.parse("application/json")))
                .header("Authentication", authToken)
                .build();
        try (Response res = client.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("Failed to prepare upload");
            }

            JsonNode parsed = json.parse(Objects.requireNonNull(res.body()).string());
            UploadResult result = new UploadResult();
            result.fromJSON(parsed);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare upload", e);
        }
    }

    public void deleteFile(String fileId) {
        Request request = new Request.Builder()
                .url(uploadServer + "/api/v1/delete/" + fileId)
                .header("Authentication", authToken)
                .build();
        try (Response res = client.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("Failed to delete file");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public String idFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
