package de.glowman554.search;

import de.glowman554.search.data.SearchResult;
import de.glowman554.search.data.User;
import de.glowman554.search.data.UserConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;

public class BackendDatabaseConnection extends BaseDatabaseConnection {
    private final int resultsPerPage = 10;

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
}
