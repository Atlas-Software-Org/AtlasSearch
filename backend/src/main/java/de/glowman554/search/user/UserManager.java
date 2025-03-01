package de.glowman554.search.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.glowman554.search.data.User;
import de.glowman554.search.database.BackendDatabase;

import java.security.SecureRandom;
import java.sql.SQLException;

public class UserManager {
    private final BackendDatabase database;

    public UserManager(BackendDatabase database) {
        this.database = database;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean verifyPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }

    private String createToken() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!$%&/()";

        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        return token.toString();
    }

    private String createSession(String username) throws SQLException {
        String token = createToken();
        database.users.createNewSession(username, token);
        return token;
    }

    public String registerAndCreateSession(String username, String password) throws SQLException {
        PasswordValidator.RejectionReason rejectionReason = PasswordValidator.testPassword(password);
        if (rejectionReason != null) {
            throw new IllegalArgumentException(rejectionReason.getMessage());
        }
        String passwordHash = hashPassword(password);
        String defaultProfilePicture = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Anthro_vixen_colored.jpg/220px-Anthro_vixen_colored.jpg";
        database.users.createNewUser(username, passwordHash, defaultProfilePicture);
        return createSession(username);
    }

    public String loginAndCreateSession(String username, String password) throws SQLException {
        User user = database.users.loadUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!verifyPassword(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return createSession(username);
    }

    public void changePasswordAndDeleteSessions(User user, String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }
        PasswordValidator.RejectionReason rejectionReason = PasswordValidator.testPassword(newPassword);
        if (rejectionReason != null) {
            throw new IllegalArgumentException(rejectionReason.getMessage());
        }
        String passwordHash = hashPassword(newPassword);
        database.users.updateUserPassword(user.getUsername(), passwordHash);
        database.users.deleteUserSessions(user.getUsername());
    }

    public User getSession(String token, boolean required) {
        User user = database.users.loadUserByToken(token);
        if (user == null && required) {
            throw new RuntimeException("Not authenticated");
        }
        return user;
    }
}
