package primaryFiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Movie> table;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, LocalTime> lengthColumn;
    @FXML
    private Button addMovieButton;
    @FXML
    private Label connection;
    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField hourField;
    @FXML
    private TextField minuteField;

    private Model model = new Model();
    private ObservableList<Movie> data;

    @FXML
    public void addMovie(ActionEvent event){
        int hours, minutes;
        try{
            hours = Integer.parseInt(hourField.getText());
            minutes = Integer.parseInt(minuteField.getText());
        }
        catch (NumberFormatException e){
            return;
        }
        minutes = minutes + (hours * 60);
        table.getItems().add(new Movie(titleField.getText(), genreField.getText(),model.intToLocalTime(minutes)));
        titleField.setText("");
        genreField.setText("");
        hourField.setText("");
        minuteField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Movie, LocalTime> ("length"));

        if(model.isConnected()){
            connection.setText("Connected");
        }
        else{
            connection.setText("Not connected");
        }

        buildData();
    }

    public void buildData(){
        Connection conn = model.getConnection();
        try {
            data = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Movies";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Movie movie = new Movie();
                movie.setTitle(rs.getString("Title"));
                movie.setGenre(rs.getString("Genre"));
                movie.setLength(model.intToLocalTime(rs.getInt("Length")));
                data.add(movie);
            }
            table.setItems(data);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error building data");
        }
    }
}
