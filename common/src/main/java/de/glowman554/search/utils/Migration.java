package de.glowman554.search.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Migration {
    private final String id;
    private final Apply apply;

    public Migration(String id, Apply apply, Connection connection) throws SQLException {
        this.id = id;
        this.apply = apply;

        createMigrationTable(connection);
        apply(connection);
    }

    private void createMigrationTable(Connection connection) throws SQLException {
        Statement s = connection.createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS __migrations (id VARCHAR(255) PRIMARY KEY)");
        s.close();
    }

    private boolean isApplied(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        st.execute("SELECT count(*) FROM __migrations WHERE id = '" + id + "'");
        ResultSet rs = st.getResultSet();
        rs.next();
        boolean applied = rs.getInt(1) > 0;
        rs.close();
        st.close();
        return applied;
    }

    private void apply(Connection connection) throws SQLException {
        if (!isApplied(connection)) {
            Logger.log("Applying migration " + id);

            Statement applStatement = connection.createStatement();
            apply.apply(connection, applStatement);
            applStatement.close();

            Statement st = connection.createStatement();
            st.execute("INSERT INTO __migrations (id) VALUES ('" + id + "')");
        }
    }


    public interface Apply {
        void apply(Connection connection, Statement statement) throws SQLException;
    }

}
