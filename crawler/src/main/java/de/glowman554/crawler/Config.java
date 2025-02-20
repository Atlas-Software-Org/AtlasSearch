package de.glowman554.crawler;

import de.glowman554.config.ConfigFile;
import de.glowman554.config.Savable;
import de.glowman554.config.auto.Saved;
import de.glowman554.search.DatabaseConfig;

import java.io.File;

public class Config extends ConfigFile {
    @Saved
    private String[] startingPoints = new String[]{"https://glowman554.de/"};

    @Saved(remap = Savable.class)
    private DatabaseConfig database = new DatabaseConfig();

    public Config() {
        super(new File("crawler.json"));
    }

    public String[] getStartingPoints() {
        return startingPoints;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }
}
