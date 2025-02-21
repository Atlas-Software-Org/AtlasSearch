package de.glowman554.search.utils;

public class MutedException extends RuntimeException {
    public MutedException(String message) {
        super(message);
    }

    public MutedException(Throwable cause) {
        super(cause);
    }
}
