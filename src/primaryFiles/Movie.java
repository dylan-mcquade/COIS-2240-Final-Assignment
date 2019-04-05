package primaryFiles;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalTime;

public class Movie {
    //The three variables that make up a movie, stored as property for interactions with the table.
    private SimpleStringProperty title, genre;
    //LocalTime is used because its default format when printed in the table is easy to read, suits displaying some length of time and can be automatically sorted.
    private SimpleObjectProperty<LocalTime> length;

    //Constructor that takes in 3 parameters and assigns them to the appropriate variables.
    public Movie (String title, String genre, LocalTime length){
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.length = new SimpleObjectProperty<LocalTime>(length);
    }

    //Default constructor. Leaves all variables as new objects.
    public Movie(){
        this.title = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.length = new SimpleObjectProperty<LocalTime>();
    }


    //Getters and setters.
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setLength(LocalTime length) {
        this.length.set(length);
    }
}
