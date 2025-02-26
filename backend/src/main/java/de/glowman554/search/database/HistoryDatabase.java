package de.glowman554.search.database;

import de.glowman554.crawler.core.Crawler;
import de.glowman554.search.BaseDatabaseConnection;
import de.glowman554.search.data.CrawlHistoryEntry;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class HistoryDatabase extends BaseDatabaseConnection {
    public HistoryDatabase(BaseDatabaseConnection base) {
        super(base);
    }

    public void insertSearchHistory(String username, String query) {
        tryExecute("INSERT INTO searchHistory (username, query) VALUES (?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, query);
            statement.execute();
        });
    }

    public void insertVisitHistory(String username, String link) {
        tryExecute("INSERT INTO visitHistory (username, link) VALUES (?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, link);
            statement.execute();
        });
    }

    public void insertCrawlRequest(String username, String link, Crawler.CrawlerStatus status) {
        tryExecute("INSERT INTO crawlRequests (username, url, status) VALUES (?, ?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, link);
            statement.setString(3, status.toString());
            statement.execute();
        });
    }

    public ArrayList<CrawlHistoryEntry> loadCrawlHistory() {
        return tryExecute("SELECT username, id, url, timestamp, status FROM crawlRequests ORDER BY timestamp DESC limit 100", PreparedStatement::execute, resultSet -> {
            ArrayList<CrawlHistoryEntry> history = new ArrayList<>();
            while (resultSet.next()) {
                history.add(new CrawlHistoryEntry(resultSet.getString("username"), resultSet.getInt("id"), resultSet.getString("url"), resultSet.getTimestamp("timestamp"), resultSet.getString("status")));
            }
            return history;
        });
    }

}
