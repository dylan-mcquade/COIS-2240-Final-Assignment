package primaryFiles;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Movie {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private SimpleStringProperty title, genre;
    private SimpleObjectProperty<LocalTime> length;

    public Movie (String title, String genre, LocalTime length){
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.length = new SimpleObjectProperty<LocalTime>(length);
    }
}
