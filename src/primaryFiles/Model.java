package primaryFiles;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Model {
    Connection connection;

    public Model() {
        connection = DatabaseConnection.connect();
        if (connection == null) {
            System.exit(1);
        }
    }

    public boolean isConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LocalTime intToLocalTime(int totalMinutes){
        int hours = 0;
        int minutes = 0;
        while(totalMinutes >= 60){
            totalMinutes-=60;
            hours+=1;
        }
        minutes = totalMinutes;
        LocalTime length = LocalTime.of(hours,minutes);
        return length;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
