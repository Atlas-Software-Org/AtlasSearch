package de.glowman554.search.user;

public class PasswordValidator {

    public static RejectionReason testPassword(String password) {
        if (password.length() < 8) {
            return RejectionReason.TOO_SHORT;
        }
        if (!password.matches(".*\\d.*")) {
            return RejectionReason.NO_DIGIT;
        }
        if (!password.matches(".*[A-Z].*")) {
            return RejectionReason.NO_UPPERCASE;
        }
        if (!password.matches(".*[a-z].*")) {
            return RejectionReason.NO_LOWERCASE;
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return RejectionReason.NO_SPECIAL_CHAR;
        }
        return null;
    }

    public static enum RejectionReason {
        TOO_SHORT("Password must be at least 8 characters long"),
        NO_DIGIT("Password must contain at least one digit"),
        NO_UPPERCASE("Password must contain at least one uppercase letter"),
        NO_LOWERCASE("Password must contain at least one lowercase letter"),
        NO_SPECIAL_CHAR("Password must contain at least one special character");

        private final String message;

        private RejectionReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
