package moviebookingapp.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import moviebookingapp.controllers.showtimeseat.ShowtimeseatController;
import moviebookingapp.dao.BookingReservationDAO;
import moviebookingapp.dao.FoodDrinkDao;
import moviebookingapp.dao.SeatDAO;
import moviebookingapp.dao.ShowTimeDAO;
import moviebookingapp.entity.*;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class FoodDrinkController implements Initializable {
    public Text staffName;
    public TableColumn<FoodDrink, HBox> qtyTbCl;
    public TableColumn<FoodDrink, String> priceTbCl;
    public TableColumn<FoodDrink, String> productTbCl;
    public TableColumn<FoodDrink, ImageView> imageTbCl;
    public TableView<FoodDrink> tbView;
    public Text totalPrice;
    public ScrollPane foodRevPane;
    public Button invoice;
    public Button logOutBtn;
    public Hyperlink backBtn;
    private Stage currentStage;
    private List<FoodDrink> foodDrink_list;
    private List<HBox> foodDrinkRevHb_list = new ArrayList<>();
    private int total_price = 0;
    public static Movie selected_movie; // sử dụng để truyền lại showtimeSeatController khi nhấn back
    public void setStage(Stage currentStage, Movie selected_movie) {
        this.currentStage = currentStage;
        FoodDrinkController.selected_movie = selected_movie;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        staffName.setText(LoginFormController.activeUser.getUsername());
        foodRevPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        totalPrice.setText("0 đ");
        try {
            foodDrink_list = new FoodDrinkDao().list();
            ObservableList<FoodDrink> observableList = FXCollections.observableList(foodDrink_list);
            //re-create orderRev
            for (FoodDrink foodDrink : foodDrink_list) {
                if (foodDrink.getOrder_quantity() > 0) {
                    foodDrinkRevHb_list.add(createFoodDrinkHbox(foodDrink));
                    total_price += foodDrink.getOrder_quantity() * foodDrink.getPrice();
                }
            }
            VBox foodRevContent = new VBox();
            System.out.println(foodDrinkRevHb_list);
            foodRevContent.getChildren().addAll(foodDrinkRevHb_list);
            foodRevPane.setContent(foodRevContent);
            totalPrice.setText(total_price + " đ");
            //re-create orderRev
            //create list of foodDrink

            qtyTbCl.setCellValueFactory(new PropertyValueFactory<>("qtyHBox"));
            imageTbCl.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            productTbCl.setCellValueFactory(new PropertyValueFactory<>("product"));
            priceTbCl.setCellValueFactory(new PropertyValueFactory<>("priceString"));
            tbView.setItems(observableList);
            //create list of foodDrink

            for(FoodDrink foodDrink: foodDrink_list) {
                TextField qtyTextField = foodDrink.getQtyTf();// phải ở trên thì mới access đc bên trong incre handler

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    // Use a regular expression to allow only numeric input
                    if (Pattern.matches("[0-9]*", change.getControlNewText())) {
                        return change;
                    } else {
                        return null; // Reject the change if it contains non-numeric characters
                    }
                };
                TextFormatter<String> textFormatter = new TextFormatter<>(filter);

                qtyTextField.setTextFormatter(textFormatter);// set to force user input number
                qtyTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        // This code will be executed when the Enter key is pressed
                        int currentInput = 0;
                        try {
                            currentInput = Integer.parseInt(qtyTextField.getText());
                        }catch (NumberFormatException e) {
                            currentInput = 0;
                        }
                        if(currentInput > 0 && foodDrink.getOrder_quantity() <= 0) {
                            foodDrink.setOrder_quantity(currentInput);
                            addFoodDrinkHbox(foodDrink);
                        }
                        else if(currentInput <= 0) {
                            foodDrink.setOrder_quantity(0);
                            qtyTextField.setText(0+"");
                            removeFoodDrinkHbox(foodDrink);
                        }else if(currentInput != 0 && foodDrink.getOrder_quantity() != 0) {
                            foodDrink.setOrder_quantity(currentInput);
                            updateFoodDrinkHbox(foodDrink);
                        }
                        new FoodDrinkDao().update(foodDrink);
                        updateTotalPrice();
                    }
                });

                Button decreBtn = foodDrink.getDecreBtn();
                decreBtn.setOnMouseClicked(event -> {
                    int currentInput = 0;
                    try {
                        currentInput = Integer.parseInt(qtyTextField.getText());
                    }catch(NumberFormatException e) {
                        currentInput = 0;
                    }
                    if(currentInput <= 1) {
                        foodDrink.setOrder_quantity(0);

                        removeFoodDrinkHbox(foodDrink);
                        qtyTextField.setText(0+"");
                    }else {//update
//                        int setInput = currentInput - 1;
                        foodDrink.setOrder_quantity(--currentInput);
                        updateFoodDrinkHbox(foodDrink);
                        qtyTextField.setText(currentInput+"");
                    }
                    new FoodDrinkDao().update(foodDrink);
                    updateTotalPrice();
                });
                Button increBtn = foodDrink.getIncreBtn();
                increBtn.setOnMouseClicked(event -> {
                    int currentInput = 0;
                    try{

                        currentInput = Integer.parseInt(qtyTextField.getText());
                    }catch (NumberFormatException e) {
                        currentInput = 0;
                    }
                    if(currentInput <= 0) {
                        foodDrink.setOrder_quantity(1);

                        addFoodDrinkHbox(foodDrink);
                        qtyTextField.setText(1+"");
                    }else {//update
//                        int setInput = currentInput - 1;
                        foodDrink.setOrder_quantity(++currentInput);
                        updateFoodDrinkHbox(foodDrink);
                        qtyTextField.setText(currentInput+"");
                    }
                    new FoodDrinkDao().update(foodDrink);
                    updateTotalPrice();

                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFoodDrinkHbox(FoodDrink foodDrink) {
        for (HBox item : foodDrinkRevHb_list) {
            if (item.getId().equals(foodDrink.getId()+"")) {
                return;// tồn tại rồi ko thêm
            }
        }
        HBox hbox = createFoodDrinkHbox(foodDrink);
        hbox.setId(foodDrink.getId()+"");
        foodDrinkRevHb_list.add(hbox);

        VBox content = new VBox();
        content.getChildren().addAll(foodDrinkRevHb_list);
        foodRevPane.setContent(content);
    }
    public void updateTotalPrice() {
        int total_price=0;
        for(FoodDrink foodDrink: foodDrink_list) {
            total_price+=foodDrink.getOrder_quantity()* foodDrink.getPrice();
            totalPrice.setText(total_price+" đ");
        }
    }
    public void updateFoodDrinkHbox(FoodDrink foodDrink) {
//        try {
//        HBox updatedHbox = foodDrinkRevHb_list.stream().filter((item) -> {
//            if (item.getId().equals(foodDrink.getId() + "")) return true;
//            else return false;
//        }).findFirst().orElseThrow(() -> new IllegalStateException("HBox not present"));
//            int index = foodDrinkRevHb_list.indexOf(updatedHbox);
//            foodDrinkRevHb_list.set(index, createFoodDrinkHbox(foodDrink));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        int index = 0;
        for(HBox hbox : foodDrinkRevHb_list) {
            if(hbox.getId().equals(foodDrink.getId()+"")) {
                foodDrinkRevHb_list.set(index, createFoodDrinkHbox(foodDrink));
                VBox content= new VBox();
                content.getChildren().addAll(foodDrinkRevHb_list);
                foodRevPane.setContent(content);
                break;
            }
            index++;
        }

    }
    public void removeFoodDrinkHbox(FoodDrink foodDrink) {
        try {
            foodDrinkRevHb_list.remove(foodDrinkRevHb_list.stream().filter((item) -> {
                if(item.getId().equals(foodDrink.getId()+"")) return true;
                else return false;
            } ).findFirst().orElseThrow(() -> new IllegalStateException("HBox not present")));//lấy Hbox ra khỏi Optional<Hbox> list
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox content= new VBox();
        content.getChildren().addAll(foodDrinkRevHb_list);
        foodRevPane.setContent(content);
    }
    public HBox createFoodDrinkHbox(FoodDrink foodDrink) {
        HBox foodDrink_Hb= new HBox();
        foodDrink_Hb.setId(foodDrink.getId()+"");
        foodDrink_Hb.getStyleClass().add("food-rev-hb");
        Label product_text = new Label(foodDrink.getProduct());
        product_text.getStyleClass().add("hb-product-name");
        Label qty_text = new Label("x"+foodDrink.getOrder_quantity());
        qty_text.getStyleClass().add("hb-order-qty");
        Label price_text = new Label(foodDrink.getPrice()+" đ");
        price_text.getStyleClass().add("hb-price");
        foodDrink_Hb.getChildren().addAll(product_text,qty_text, price_text);


        return foodDrink_Hb;
    }
    public void backToShowTimeList(ActionEvent actionEvent) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/oldFxml/showtimeseat.fxml"));
                Parent root = loader.load();
                ShowtimeseatController showtimeseatController = loader.getController();

                Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                showtimeseatController.setMovieInfo(selected_movie, currentStage);

                currentStage.setTitle("Schedule & Seating");
                Scene scene = new Scene(root, 1600, 900 );
                // Load the CSS file
                scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());
                currentStage.setScene(scene);
            }catch (Exception e){
                e.printStackTrace();
            }
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


    public void gotoInvoice(ActionEvent actionEvent) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/InvoiceSummary.fxml"));
                Parent root = loader.load();
                InvoiceSummaryController invoiceSummaryController = loader.getController();

                Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                invoiceSummaryController.setState(currentStage);

                currentStage.setTitle("Invoice & Summary");
                Scene scene = new Scene(root, 1600, 900 );
                // Load the CSS file
                scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());
                currentStage.setScene(scene);
            }catch (Exception e){
                e.printStackTrace();
            }

    }
}
