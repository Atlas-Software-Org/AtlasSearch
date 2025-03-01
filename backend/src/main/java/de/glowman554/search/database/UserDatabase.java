package de.glowman554.search.database;

import de.glowman554.search.BaseDatabaseConnection;
import de.glowman554.search.data.PartialUser;
import de.glowman554.search.data.User;
import de.glowman554.search.data.UserConfiguration;
import de.glowman554.search.data.UserMetadata;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDatabase extends BaseDatabaseConnection {

    public UserDatabase(BaseDatabaseConnection base) {
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
        return tryExecute("SELECT username, profilePictureUrl, passwordHash, isAdministrator, isPremiumUser, shouldKeepHistory, shouldUseAi FROM users WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            UserConfiguration configuration = new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"), resultSet.getBoolean("shouldUseAi"));
            return new User(resultSet.getString("username"), resultSet.getString("profilePictureUrl"), resultSet.getString("passwordHash"), resultSet.getBoolean("isAdministrator"), resultSet.getBoolean("isPremiumUser"), configuration);
        });
    }

    public User loadUserByToken(String token) {
        return tryExecute("SELECT users.username, profilePictureUrl, passwordHash, isAdministrator, isPremiumUser, shouldKeepHistory, shouldUseAi FROM users, userSessions WHERE users.username = userSessions.username AND token = ?", statement -> {
            statement.setString(1, token);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            UserConfiguration configuration = new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"), resultSet.getBoolean("shouldUseAi"));
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

    public void updateUserMetadata(UserMetadata metadata) {
        tryExecute("UPDATE users SET isAdministrator = ?, isPremiumUser = ? WHERE username = ?", statement -> {
            statement.setBoolean(1, metadata.isAdministrator());
            statement.setBoolean(2, metadata.isPremiumUser());
            statement.setString(3, metadata.getUsername());
            statement.execute();
        });
    }

    public void deleteUser(String username) {
        tryExecute("DELETE FROM users WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        });
    }

    public void deleteUserSessions(String username) {
        tryExecute("DELETE FROM userSessions WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        });
    }

/*
    public UserConfiguration loadUserConfiguration(String username) {
        return tryExecute("SELECT shouldKeepHistory, shouldUseAi FROM users WHERE username = ?", statement -> {
            statement.setString(1, username);
            statement.execute();
        }, resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            return new UserConfiguration(resultSet.getBoolean("shouldKeepHistory"), resultSet.getBoolean("shouldUseAi"));
        });
    }
*/

    public ArrayList<PartialUser> loadUserList() {
        return tryExecute("SELECT username, profilePictureUrl FROM users", PreparedStatement::execute, resultSet -> {
            ArrayList<PartialUser> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new PartialUser(resultSet.getString("username"), resultSet.getString("profilePictureUrl")));
            }
            return users;
        });
    }

}
