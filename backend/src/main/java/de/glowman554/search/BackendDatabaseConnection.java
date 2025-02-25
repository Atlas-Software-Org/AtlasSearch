package de.glowman554.search;

import de.glowman554.crawler.core.Crawler;
import de.glowman554.search.data.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class BackendDatabaseConnection extends BaseDatabaseConnection {
    private final int resultsPerPage = 10;
    private final int latenciesToLoad = 30;

    public BackendDatabaseConnection(DatabaseConfig config) throws SQLException {
        super(config);
    }

    public BackendDatabaseConnection(BaseDatabaseConnection base) {
        super(base);
    }

    public void createNewUser(String username, String passwordHash, String profilePictureUrl) throws SQLException {
        execute("INSERT INTO users (username, passwordHash, profilePictureUrl) VALUES (?, ?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.setString(3, profilePictureUrl);
            statement.execute();
        });
    }

    public void createNewSession(String username, String token) throws SQLException {
        execute("INSERT INTO userSessions (username, token) VALUES (?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, token);
            statement.execute();
        });
    }

    public User loadUser(String username) {
        return tryExecute("SELECT username, profilePictureUrl, passwordHash, isAdministrator, isPremiumUser, shouldKeepHistory FROM users WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            UserConfiguration configuration = new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"));
            return new User(resultSet.getString("username"), resultSet.getString("profilePictureUrl"), resultSet.getString("passwordHash"), resultSet.getBoolean("isAdministrator"), resultSet.getBoolean("isPremiumUser"), configuration);
        });
    }

    public User loadUserByToken(String token) {
        return tryExecute("SELECT users.username, profilePictureUrl, passwordHash, isAdministrator, isPremiumUser, shouldKeepHistory FROM users, userSessions WHERE users.username = userSessions.username AND token = ?", statement -> {
            statement.setString(1, token);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            UserConfiguration configuration = new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"));
            return new User(resultSet.getString("username"), resultSet.getString("profilePictureUrl"), resultSet.getString("passwordHash"), resultSet.getBoolean("isAdministrator"), resultSet.getBoolean("isPremiumUser"), configuration);
        });
    }

    public void updateUserProfilePicture(String username, String profilePictureUrl) {
        tryExecute("UPDATE users SET profilePictureUrl = ? WHERE username = ?", statement -> {
            statement.setString(1, profilePictureUrl);
            statement.setString(2, username);
            statement.execute();
        });
    }

    public void updateUserPassword(String username, String passwordHash) {
        tryExecute("UPDATE users SET passwordHash = ? WHERE username = ?", statement -> {
            statement.setString(1, passwordHash);
            statement.setString(2, username);
            statement.execute();
        });
    }

    public void deleteUserSessions(String username) {
        tryExecute("DELETE FROM userSessions WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        });
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

    public UserConfiguration loadUserConfiguration(String username) {
        return tryExecute("SELECT shouldKeepHistory FROM users WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            return new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"));
        });
    }

    public ArrayList<SearchResult> performSearch(String query, int page) throws SQLException {
        return execute("""
                SELECT link, title, description, shortText, MATCH (`title`, `link`, `content`, `description`, `keywords`) AGAINST (?) as `score`
                FROM webPages WHERE MATCH (`title`, `link`, `content`, `description`, `keywords`) AGAINST (?)
                ORDER BY `score` DESC LIMIT ? OFFSET ?
                """, statement -> {
            statement.setString(1, query);
            statement.setString(2, query);
            statement.setInt(3, resultsPerPage);
            statement.setInt(4, page * resultsPerPage);
            statement.execute();
        }, resultSet -> {
            ArrayList<SearchResult> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(new SearchResult(
                        resultSet.getString("link"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("shortText"),
                        resultSet.getDouble("score")));
            }
            return results;
        });
    }

    public void insertTimingEvent(String username, String key, int time) {
        tryExecute("INSERT INTO timingEvents (username, timingKey, duration) VALUES (?, ?, ?)", statement -> {
            statement.setString(1, username);
            statement.setString(2, key);
            statement.setLong(3, time);
            statement.execute();
        });
    }

    public HashMap<String, ArrayList<TimingAverageEntry>> loadTimingAverages() {
        return tryExecute("""
                select floor(avg(duration)) as latency, timingKey, year(timestamp) as y, month(timestamp) as m, day(timestamp) as d
                from timingEvents group by timingKey, year(timestamp), month(timestamp), day(timestamp)
                order by year(timestamp), month(timestamp), day(timestamp) desc limit ?;
                """, statement -> {
            statement.setInt(1, latenciesToLoad);
            statement.execute();
        }, resultSet -> {
            HashMap<String, ArrayList<TimingAverageEntry>> events = new HashMap<>();
            while (resultSet.next()) {
                String key = resultSet.getString("timingKey");

                if (!events.containsKey(key)) {
                    events.put(key, new ArrayList<>());
                }

                String date = resultSet.getInt("y") + "-" + resultSet.getInt("m") + "-" + resultSet.getInt("d");
                events.get(key).add(new TimingAverageEntry(date, resultSet.getInt("latency")));
            }
            return events;
        });
    }
}
