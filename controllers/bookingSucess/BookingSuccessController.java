package moviebookingapp.controllers.bookingSucess;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import moviebookingapp.controllers.LoginFormController;
import moviebookingapp.controllers.MovieListController;

public class BookingSuccessController {

    private Stage currentStage;
    public void setStage(Stage stage) {
        currentStage = stage;
    }
    public void backToMovieList(ActionEvent actionEvent) {
        try{
//                System.out.println("in the button");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/moviecinema.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1600, 900);
            scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());

            currentStage.setTitle("Movies Cinema");
            currentStage.setScene(scene);
            currentStage.show();
            MovieListController movieListController = loader.getController();
            movieListController.setStage(currentStage);


//                System.out.println("here btn Clicked");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void logOut(ActionEvent actionEvent) {
        try {
            // Create a confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation Dialog");
            confirmationDialog.setHeaderText("Are you sure you want to logout?");

            // Show the confirmation dialog and wait for user response
            confirmationDialog.showAndWait().ifPresent(response -> {
                try{
                    if (response == ButtonType.OK) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/loginform.fxml"));
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


    public void setDarkMode(ActionEvent actionEvent) {
    }

    public void setLightMode(ActionEvent actionEvent) {
    }


}
