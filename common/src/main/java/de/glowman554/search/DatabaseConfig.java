package de.glowman554.search;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class DatabaseConfig extends AutoSavable {
    @Saved
    private String host = "localhost";
    @Saved
    private String database = "search";
    @Saved
    private String username = "root";
    @Saved
    private String password = "";

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
