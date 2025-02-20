package de.glowman554.search;

import de.glowman554.config.ConfigFile;
import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

import java.io.File;

public class Config extends ConfigFile {
    @Saved(remap = Savable.class)
    private WebserverConfig webserver = new WebserverConfig();

    @Saved(remap = Savable.class)
    private DatabaseConfig database = new DatabaseConfig();

    public Config() {
        super(new File("config.json"));
    }

    public WebserverConfig getWebserver() {
        return webserver;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public static class WebserverConfig extends AutoSavable {
        @Saved
        private int port = 8888;
        @Saved
        private String keystoreFile = "";
        @Saved
        private String keystorePassword = "";

        public boolean isSSL() {
            return !(keystoreFile.isEmpty() || keystorePassword.isEmpty());
        }

        public int getPort() {
            return port;
        }

        public String getKeystoreFile() {
            return keystoreFile;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }
    }
}
