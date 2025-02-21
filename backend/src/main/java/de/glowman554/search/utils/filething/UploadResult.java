package de.glowman554.search.utils.filething;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class UploadResult extends AutoSavable {
    @Saved
    private String uploadToken;
    @Saved
    private String id;
    @Saved
    private String url;

    public String getUploadToken() {
        return uploadToken;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
