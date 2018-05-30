package dao;
import java.io.*;
import java.sql.*;

public class DbConnection {
//    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static Connection conn = null;
    private DbConnection() {}
    private static String db_url = "dbres/In-flight_Entertainer.db";

    public static void setDbUrl(String url) {
        db_url = url;
    }
    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + db_url);
                System.out.println("Connected");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return conn;
    }

    public static void excute(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static ResultSet query(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

}
