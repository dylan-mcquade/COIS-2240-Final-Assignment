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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

//Handles user inputs. The "control" in MVC.
public class Controller implements Initializable {
    //FXML controls. Parts of the UI that the code interacts with.

    //The table which stores the movies.
    @FXML
    private TableView<Movie> table;
    //Column of the table with the title of the movie.
    @FXML
    private TableColumn<Movie, String> titleColumn;
    //Column of the table with the genre of the movie.
    @FXML
    private TableColumn<Movie, String> genreColumn;
    //Column of the table with the length of the movie.
    @FXML
    private TableColumn<Movie, LocalTime> lengthColumn;
    //Where the user enters the title of a movie they want to add to the table.
    @FXML
    private TextField titleField;
    //Where the user enters the genre of a movie they want to add to the table.
    @FXML
    private TextField genreField;
    //Where the user enters how many hours are in the length of the movie.
    @FXML
    private TextField hourField;
    //Where the user enters how many minutes are in the length of the movie.
    @FXML
    private TextField minuteField;
    //Where important information is displayed to the user. Main way of displaying information.
    @FXML
    private TextArea programInformation;
    //Where the user enters the genre of a random movie they want to get.
    @FXML
    private TextField genreChoice;

    //model is used to access methods in the Model class which control program locic. The "model" in MVC.
    private Model model = new Model();
    private ObservableList<Movie> movieList;

    //If a user has all 4 fields (movie title, movie genre, hours in length, minutes in length) and presses the "Add Movie" button this method will add a movie with
    //the given information both into the table for use with this session and into the database so it will be available on later uses.
    @FXML
    public void addMovie(ActionEvent event){
        //Establish the connection to the database.
        model.reconnect();
        //Check to make sure that the title and genre are provided.
        if(!(titleField.getText().isEmpty()) && !(genreField.getText().isEmpty())){
            int hours, minutes;
            //Make sure that the lengths provided are given in integers. Hours have to be less than 24, minutes less than 60.
            //If the lengths are valid then hours and minutes will hold them before they are put into the table and database.
            try {
                if((Integer.parseInt(hourField.getText()) <24) && (Integer.parseInt(minuteField.getText()) <60)) {
                    hours = Integer.parseInt(hourField.getText());
                    minutes = Integer.parseInt(minuteField.getText());
                }
                //If the lengths are too long programInformation will be updated with a warning that tells the user the max times.
                else{
                    programInformation.setText("\"Duration : Hours\" cannot be greater than 23 and \"Duration : Minutes\" cannot be greater than 59");
                    return;
                }
            //If the lengths aren't integers programInformation will be updated with a warning that tells the user that they may only enter integers.
            } catch (NumberFormatException e) {
                programInformation.setText("\"Duration : Hours\" and \"Duration : Minutes\" must be filled out using digits 0 through 9. " +
                        "Letters and other symbols are not supported");
                clearFields();
                return;
            }
            //Length in the database is stored as a single integer, the total number of minutes.
            minutes = minutes + (hours * 60);
            table.getItems().add(new Movie(titleField.getText(), genreField.getText(), model.intToLocalTime(minutes)));
            String sql = "INSERT INTO Movies(Title,Genre,Length) VALUES(?,?,?)";
            try (Connection conn = model.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                 preparedStatement.setString(1, titleField.getText());
                 preparedStatement.setString(2, genreField.getText());
                 preparedStatement.setInt(3, minutes);
                 preparedStatement.executeUpdate();
                 clearFields();
                 return;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        //If the title or genre are not filled out then programInformation is updated to tell the user that they need to be filled out.
        programInformation.setText("\"Title\" and \"Genre\" must be filled out.");
        clearFields();
    }

    //Removes a movie from both the database and the table if the user clicks on the movie and then presses the "Remove Movie" button.
    public void removeMovie(ActionEvent event){
        //Establish connection.
        model.reconnect();
        //Check that there is a selected movie, if yes, then continue to remove it.
        if(table.getSelectionModel().getSelectedItem() != null){
            //Use a movie variable and set it to the selected movie. Use this to remove it from the table and use it's title to remove it from the database.
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
    }

    //Clears the four fields used for new movie entry. Any time the user tries to add a movie, either successfully or not, it is called.
    public void clearFields(){
        titleField.setText("");
        genreField.setText("");
        hourField.setText("");
        minuteField.setText("");
        genreChoice.setText("");
    }

    //Puts the title of a random movie into programInformation when the user hits the "Random Movie" button.
    public void randomMovie(){
        //Check if there are any movies to choose from. If not, inform the user and end the method.
        if(table.getItems().isEmpty()){
            programInformation.setText("There aren't any movies to pick from.");
            return;
        }
        //Obtain the size of the table.
        int count;
        count = table.getItems().size();
        //Get a random number between 0 (first index of the table) and whatever the size of the table is (final index of the table).
        int randomIndex = ThreadLocalRandom.current().nextInt(0, count);
        //Use this random number to access some movie from the table and put its title in programInformation.
        programInformation.setText(movieList.get(randomIndex).getTitle());
    }

    //Gets a random movie of an entered genre and puts its title into programInformation.
    public void randomMovieGenre(){

        //Check if there are any movies to choose from. If not, inform the user and end the method.
        if(table.getItems().isEmpty()){
            programInformation.setText("There aren't any movies to pick from.");
            return;
        }
        //Check that the user has entered a genre.
        if(!genreChoice.getText().isEmpty()) {
            //Create a list of movies and fill it with all movies from the table that match the genre typed into "genreChoice".
            List<Movie> genreList = new ArrayList<Movie>();
            for (Movie m : movieList) {
                if (m.getGenre().equals(genreChoice.getText())) {
                    genreList.add(m);
                }
            }
            //Check if any movies of the chosen genre were found. If not, update programInformation to inform the user and end the method.
            if(genreList.isEmpty()){
                programInformation.setText("No movies of the chosen genre were found.");
                clearFields();
                return;
            }
            //Obtain the size of the list of movies of the chosen genre.
            int count = genreList.size();
            //Pick a random number between 0 (first index of this list) and the size of the list (final index of this list).
            int randomIndex = ThreadLocalRandom.current().nextInt(0, count);
            //Put the title of the movie found at the randomly selected index into programInformation.
            clearFields();
            programInformation.setText(genreList.get(randomIndex).getTitle());
        }
        //If the user hasn't entered a genre update programInformation to tell them that they must do so.
        else{
            programInformation.setText("Please enter a genre");
        }
    }

    //Runs when program starts.
    @Override
    public void initialize(URL location, ResourceBundle rb){
        //Set up the three columns of the table.
        titleColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Movie, LocalTime> ("length"));

        buildData();
    }

    //Create the table from whatever data may currently be in the database.
    public void buildData(){
        //Establish connection to database.
        Connection conn = model.getConnection();
        try {
            //A list of movies that will be added to the table when the program starts.
            movieList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Movies";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            //For each entry in the database create a new movie with details that match up with the database and then add that movie to movieList.
            while(rs.next()){
                Movie movie = new Movie();
                movie.setTitle(rs.getString("Title"));
                movie.setGenre(rs.getString("Genre"));
                movie.setLength(model.intToLocalTime(rs.getInt("Length")));
                movieList.add(movie);
            }
            //Add the movies in movieList to the table.
            table.setItems(movieList);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error building movieList");
        }
    }
}
