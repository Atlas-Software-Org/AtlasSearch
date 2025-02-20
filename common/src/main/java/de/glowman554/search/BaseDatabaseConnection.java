package de.glowman554.search;

import de.glowman554.search.utils.Logger;

import java.sql.*;

public class BaseDatabaseConnection {
    protected final Connection connection;

    private BaseDatabaseConnection(String url, String username, String password, String db) throws SQLException {
        connection = DriverManager
                .getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", url, db, username, password));
        Migrations.apply(connection);
    }

    public BaseDatabaseConnection(DatabaseConfig config) throws SQLException {
        this(config.getHost(), config.getUsername(), config.getPassword(), config.getDatabase());
    }

    public BaseDatabaseConnection(BaseDatabaseConnection base) {
        this.connection = base.connection;
    }

    public void execute(String query, PreparedExecutor executor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            executor.execute(statement);
        }
    }

    public <T> T execute(String query, PreparedExecutor executor, ResultSetExtractor<T> extractor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            executor.execute(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                return extractor.execute(resultSet);
            }
        }
    }

    public void tryExecute(String query, PreparedExecutor executor) {
        try {
            execute(query, executor);
        } catch (SQLException e) {
            Logger.exception(e);
        }
    }

    public void tryExecuteSilent(String query, PreparedExecutor executor) {
        try {
            execute(query, executor);
        } catch (SQLException ignored) {
        }
    }

    public <T> T tryExecute(String query, PreparedExecutor executor, ResultSetExtractor<T> extractor) {
        try {
            return execute(query, executor, extractor);
        } catch (SQLException e) {
            Logger.exception(e);
            return null;
        }
    }

    public interface PreparedExecutor {
        void execute(PreparedStatement statement) throws SQLException;
    }

    public interface ResultSetExtractor<T> {
        T execute(ResultSet resultSet) throws SQLException;
    }
}
