package de.glowman554.search.database;

import de.glowman554.search.BaseDatabaseConnection;
import de.glowman554.search.DatabaseConfig;
import de.glowman554.search.data.SearchResult;
import de.glowman554.search.data.TimingAverageEntry;
import de.glowman554.search.data.WebPage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class BackendDatabase extends BaseDatabaseConnection {
    private final int resultsPerPage = 10;

    public final HistoryDatabase history;
    public final UserDatabase users;

    public BackendDatabase(DatabaseConfig config) throws SQLException {
        super(config);
        history = new HistoryDatabase(this);
        users = new UserDatabase(this);
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
                results.add(new SearchResult(resultSet.getString("link"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getString("shortText"), resultSet.getDouble("score")));
            }
            return results;
        });
    }

    public WebPage loadWebPage(String link) {
        return tryExecute("SELECT link, title, content, description, keywords, shortText, timestamp FROM webPages WHERE link = ?", statement -> {
            statement.setString(1, link);
            statement.execute();
        }, resultSet -> {
            if (resultSet.next()) {
                return new WebPage(resultSet.getString("link"), resultSet.getString("title"), resultSet.getString("content"), resultSet.getString("description"), resultSet.getString("keywords"), resultSet.getString("shortText"), resultSet.getTimestamp("timestamp"));
            }
            return null;
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
                order by year(timestamp), month(timestamp), day(timestamp) desc;
                """, PreparedStatement::execute, resultSet -> {
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
