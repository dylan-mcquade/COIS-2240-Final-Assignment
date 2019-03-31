package primaryFiles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalTime;

public class Controller {
    @FXML
    private TableView<Movie> table;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, LocalTime> lengthColumn;
    @FXML
    private Button addMovie;

    @FXML
    public void Initialize(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Movie, LocalTime> ("length"));


    }
}
