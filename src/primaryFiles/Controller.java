package primaryFiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

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
    private Button removeMovieButton;
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
    @FXML
    private TextArea programInformation;

    private Model model = new Model();
    private ObservableList<Movie> data;

    @FXML
    public void addMovie(ActionEvent event){
        if(!(titleField.getText().isEmpty()) && !(genreField.getText().isEmpty())){
            int hours, minutes;
            try {
                if((Integer.parseInt(hourField.getText()) <24) && (Integer.parseInt(minuteField.getText()) <60)) {
                    hours = Integer.parseInt(hourField.getText());
                    minutes = Integer.parseInt(minuteField.getText());
                }
                else{
                    programInformation.setText("\"Duration : Hours\" cannot be greater than 23 and \"Duration : Minutes\" cannot be greater than 59");
                    return;
                }
            } catch (NumberFormatException e) {
                programInformation.setText("\"Duration : Hours\" and \"Duration : Minutes\" must be filled out using digits 0 through 9. " +
                        "Letters and other symbols are not supported");
                clearFields();
                return;
            }
            minutes = minutes + (hours * 60);
            table.getItems().add(new Movie(titleField.getText(), genreField.getText(), model.intToLocalTime(minutes)));
            String sql = "INSERT INTO Movies(Title,Genre,Length) VALUES(?,?,?)";
            try (Connection conn = model.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                 preparedStatement.setString(1, titleField.getText());
                 preparedStatement.setString(2, genreField.getText());
                 preparedStatement.setInt(3, minutes);
                 preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        programInformation.setText("\"Title\" and \"Genre\" must be filled out.");
        clearFields();
    }

    public void removeMovie(ActionEvent event){
        Movie movie = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(movie);
        String sql = "DELETE FROM Movies WHERE Title = ?";
        try (Connection conn = model.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearFields(){
        titleField.setText("");
        genreField.setText("");
        hourField.setText("");
        minuteField.setText("");
    }

    public void randomMovie(){
        int count;
        count = table.getItems().size();
        int randomIndex = ThreadLocalRandom.current().nextInt(0, count);
        programInformation.setText(data.get(randomIndex).getTitle());
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
