package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class ChangeProfilePictureRequest extends AutoSavable {
    @Saved
    private String fileName;

    public String getFileName() {
        return fileName;
    }
}
