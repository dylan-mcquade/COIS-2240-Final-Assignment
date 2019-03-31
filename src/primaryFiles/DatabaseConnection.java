package primaryFiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:.\\src/resources/Movies.db");
            return conn;
        } catch (SQLException e) {
            return null;
        }
    }
}
