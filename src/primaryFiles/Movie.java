package primaryFiles;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Duration;
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

    public Movie(){
        this.title = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.length = new SimpleObjectProperty<LocalTime>();
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public LocalTime getLength() {
        return length.get();
    }

    public SimpleObjectProperty<LocalTime> lengthProperty() {
        return length;
    }

    public void setLength(LocalTime length) {
        this.length.set(length);
    }
}
