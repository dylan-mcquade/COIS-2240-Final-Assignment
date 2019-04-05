package primaryFiles;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Model {
    Connection connection;

    //Default constructor. Establishes a connection wih the database.
    public Model() {
        connection = DatabaseConnection.connect();
        if (connection == null) {
            System.exit(1);
        }
    }

    //Reestablishes the connection with the database.
    public void reconnect(){
        connection = DatabaseConnection.connect();
    }

    //Converts an integer (that should represent the length of a movie in minutes) into a LocalTime of hours and minutes.
    //Used when first retrieving movie info from the database when the program is started. Length is stored as an int in the database and displayed as a LocalTime.
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

    //Getter for connection.
    public Connection getConnection() {
        return connection;
    }
}
