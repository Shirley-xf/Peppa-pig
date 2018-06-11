package dao;
import java.sql.*;

/**
 * This class interact with database.
 */
public class DbConnection {
//    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static Connection conn = null;
    private DbConnection() {}
    private static String dbUrl = "dbres/In-flight_Entertainer.db";

    /**
     * Sets db url.
     *
     * @param url the url
     */


    public static void setDbUrl(String url) {
        if (url.length() > 0) dbUrl = url;
    }
    private static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
                System.out.println("Connected");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return conn;
    }

    /**
     * Execute sql to update.
     *
     * @param sql the sql statement
     * @throws SQLException caused by malformed sql
     */
    public static void exeUpdate(String sql) throws SQLException {
        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Execute sql to query.
     *
     * @param sql the sql statement
     * @return the result set
     * @throws SQLException caused by malformed sql
     */
    public static ResultSet query(String sql) throws SQLException {
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(sql);
    }


}
