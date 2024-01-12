package moviebookingapp.controllers;

import javafx.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviebookingapp.dao.MovieDAO;
import moviebookingapp.entity.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MovieListController implements Initializable {
    public Text staffName;
    private Stage currentStage;


    @FXML
    private GridPane moviesGrid; // Inject the GridPane from FXML
    @FXML
    private ScrollPane pane; // Inject the GridPane from FXML


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        staffName.setText(LoginFormController.activeUser.getUsername());
        moviesGrid.getStyleClass().add("moviesGrid");
        // Fetch movie data from the database using MovieDAO (replace with your DAO logic)
        MovieDAO movieDAO = new MovieDAO();
        ArrayList<Movie> moviesList = movieDAO.list();

        int column = 0;
        int row = 0;

        for (Movie movie : moviesList) {
            Node moviePane = createMoviePane(movie);

            moviesGrid.add(moviePane, column, row);
//            moviesGrid.setGridLinesVisible(true);
//            moviesGrid.setAlignment(Pos.CENTER);
            column++;
            if (column == 4) { // Display 4 movies per row
                column = 0;
                row++;
            }
        }
    }

    private VBox createMoviePane(Movie movie) {
        VBox moviePane = new VBox();
        moviePane.getStyleClass().add("movie-pane");

        ImageView image = null;
        try {
            // Load the movie image (replace "/path/to/defaultImage.jpg" with a default image path)
            image = new ImageView(movie.getImage_path() != null ? movie.getImage_path() : "./moviebookingapp/asset/film_img/11.jpg");
            image.getStyleClass().add("film-image");
            image.setFitWidth(310); // Set the desired fit width
//            image.setPreserveRatio(370);
            image.setFitHeight(420);

//            image.setFitHeight(400);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Label title = new Label(movie.getTittle());
        Button btn = movie.getMovieSelectBtn();

        title.getStyleClass().add("film-title");
        btn.getStyleClass().add("film-btn");

        moviePane.getChildren().addAll(image, title, movie.getMovieSelectBtn());

        return moviePane;
    }

    public void setStage(Stage stage) {

        currentStage = stage;
    }

    public void logout(ActionEvent actionEvent) {
        try {
            // Create a confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation Dialog");
            confirmationDialog.setHeaderText("Are you sure you want to logout?");

            // Show the confirmation dialog and wait for user response
            confirmationDialog.showAndWait().ifPresent(response -> {
                try{
                    if (response == ButtonType.OK) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/loginform.fxml"));
                        Parent root = loader.load();
                        currentStage.setTitle("Movies Cinema");
                        Scene scene = new Scene(root, 1600, 900);
                        scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());

                        currentStage.setScene(scene);
                        LoginFormController loginFormController = loader.getController();
                        loginFormController.setStage(currentStage);
                        currentStage.show();

                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
