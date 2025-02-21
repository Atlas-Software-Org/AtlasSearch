package de.glowman554.search.data;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class ChangePasswordRequest extends AutoSavable {
    @Saved
    private String oldPassword;
    @Saved
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
