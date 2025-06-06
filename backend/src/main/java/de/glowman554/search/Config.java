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

    @Saved(remap = Savable.class)
    private FileThingConfig fileThing = new FileThingConfig();

    @Saved
    private String unsplashToken = "";

    @Saved
    private String openAiToken = "";

    public Config() {
        super(new File("config.json"));
    }

    public WebserverConfig getWebserver() {
        return webserver;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public FileThingConfig getFileThing() {
        return fileThing;
    }

    public String getUnsplashToken() {
        return unsplashToken;
    }

    public String getOpenAiToken() {
        return openAiToken;
    }

    public static class WebserverConfig extends AutoSavable {
        @Saved
        private int port = 8888;
        @Saved
        private String keystoreFile = "";
        @Saved
        private String keystorePassword = "";

        @Saved
        private File frontendPath = new File("frontend/dist");

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

        public File getFrontendPath() {
            return frontendPath;
        }
    }

    public static class FileThingConfig extends AutoSavable {
        @Saved
        private String authToken = "";
        @Saved
        private String uploadServer = "";

        public String getAuthToken() {
            return authToken;
        }

        public String getUploadServer() {
            return uploadServer;
        }
    }
}
