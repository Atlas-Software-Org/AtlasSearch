package de.glowman554.crawler;

import de.glowman554.crawler.core.Crawler;
import de.glowman554.crawler.core.CrawlerDatabaseConnection;
import de.glowman554.crawler.core.IPageInserter;
import de.glowman554.crawler.core.queue.AbstractQueue;
import de.glowman554.search.DatabaseConfig;
import de.glowman554.search.utils.Logger;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Config config = new Config();
        config.load();
        Logger.log("Config loaded");

        DatabaseConfig databaseConfig = config.getDatabase();
        CrawlerDatabaseConnection databaseConnection = new CrawlerDatabaseConnection(databaseConfig);

        AbstractQueue queue = databaseConnection.getQueue();
        IPageInserter inserter = databaseConnection.getInserter();

        for (String startingPoint : config.getStartingPoints()) {
            queue.insert(startingPoint);
        }

        Crawler crawler = new Crawler(queue, inserter);
        crawler.startLoop();
    }
}
