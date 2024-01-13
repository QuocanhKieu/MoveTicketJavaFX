package moviebookingapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviebookingapp.controllers.bookingSucess.BookingSuccessController;
import moviebookingapp.controllers.showtimeseat.ShowtimeseatController;
import moviebookingapp.dao.BookingReservationDAO;
import moviebookingapp.dao.FoodDrinkDao;
import moviebookingapp.dao.SeatDAO;
import moviebookingapp.entity.BookingReservation;
import moviebookingapp.entity.FoodDrink;
import moviebookingapp.entity.Seat;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InvoiceSummaryController implements Initializable {
    private final int ADULT_PRICE = 115000;
    private final int CHILD_PRICE = 100000;
    public Button logOutBtn;
    public Button PaymentBtn;
    public Hyperlink backBtn;
    public ScrollPane bookedReservationPane;
    public Text totalPrice;
    public Text ticketQtyText;
    public Text staffName;
    public ScrollPane foodRevPane;
    public Text totalPrice1;
    public Text totalPrice2;
    int total_price_2 = 0;
    private Stage currentStage;

    private List<FoodDrink> foodDrink_list;
    private List<HBox> foodDrinkRevHb_list = new ArrayList<>();
    private int total_price = 0;
    List<GridPane> reservationGridPaneList;
    int ticketQty = 0;


    public void setState(Stage stage) {
        currentStage = stage;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            staffName.setText(LoginFormController.activeUser.getUsername());
            foodRevPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            bookedReservationPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            totalPrice.setText("0 đ");
            totalPrice1.setText("0 đ");

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
            totalPrice1.setText(total_price + " đ");
            //re-create orderRev
            total_price_2 += total_price;
            total_price = 0;

            reservationGridPaneList = new ArrayList<>();
            List<BookingReservation> bookingReservationList = new BookingReservationDAO().list();
            System.out.println(bookingReservationList.size());
            for(BookingReservation rev : bookingReservationList) {
                total_price+=rev.getTicket_price();
                GridPane grid_pane = createReGridPane(rev);
                grid_pane.setId(rev.getSeat_id()+"");
                grid_pane.getStyleClass().add("rev-gridPane");


                {
                    //                gridpane.setGridLinesVisible(true);
                    ColumnConstraints column1 = new ColumnConstraints();
                    column1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                    column1.setMinWidth(10);
                    column1.setPrefWidth(250);
                    column1.setMaxWidth(Double.MAX_VALUE);

                    ColumnConstraints column2 = new ColumnConstraints();
                    column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                    column2.setMinWidth(10);
                    column2.setPrefWidth(160);
                    column2.setMaxWidth(Double.MAX_VALUE);

                    // Add ColumnConstraints to the GridPane
                    grid_pane.getColumnConstraints().addAll(column1, column2);
                }// styling block

                reservationGridPaneList.add(grid_pane);

            }
            totalPrice.setText(total_price+" đ");
            ticketQty = bookingReservationList.size();
            ticketQtyText.setText(ticketQty+" ticket(s)");

            total_price_2 += total_price;
            totalPrice2.setText(total_price_2+" đ");


            VBox re_content_1= new VBox();
            re_content_1.getChildren().addAll(reservationGridPaneList);
            bookedReservationPane.setContent(re_content_1);
//re-render the reservation side when get back to the scene END
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public GridPane createReGridPane(BookingReservation re) {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("rev-gridPane");

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                Label label = new Label();
                label.getStyleClass().add("rev-label");
                label.getStyleClass().add("rev-label" + row + col);
                if(col == 0 && row == 0){
                    label.setText(re.getMovie_title());
                }
                if(col == 1 && row == 0){
                    DateTimeFormatter formatterDM = DateTimeFormatter.ofPattern("dd/MM");
                    DateTimeFormatter formatterHM = DateTimeFormatter.ofPattern("HH:mm");

                    // Format the LocalDate to a string with only date and month
                    String showTimeDateStr = re.getShowtime_date().format(formatterDM);
                    String showTimeStart = re.getShowtimeStart_time().format(formatterHM);
                    String showTimeEnd = re.getShowtimeEnd_time().format(formatterHM);


                    label.setText(re.getSeat_name() + " " +  showTimeStart + "-" + showTimeEnd +"\n" +showTimeDateStr + "\n" + "Cinema " + re.getTheater_id());
                }
                if(col == 0 && row == 1){
                    label.setText(re.getAge_type());
                }
                if(col == 1 && row == 1){
                    label.setText(re.getTicket_price()+"đ");
                }
                if(col == 0 && row == 2){
                    switch (re.getTicket_price()) {
                        case ADULT_PRICE + 50000:
                        case CHILD_PRICE + 50000:
                            label.setText("VIP");
                            break;
                        case ADULT_PRICE * 2:
                        case CHILD_PRICE * 2:
                            label.setText("COUPLES");
                            break;
                        default:
                            label.setText("STANDARD");
                    }

                }
                GridPane.setRowIndex(label, row);
                GridPane.setColumnIndex(label, col);
                gridPane.getChildren().add(label);
            }
        }
        return gridPane;
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

    public void backToFoodDrink(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/food&drink.fxml"));
            Parent root = loader.load();
            FoodDrinkController foodDrinkController = loader.getController();

            Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            foodDrinkController.setStage(currentStage, FoodDrinkController.selected_movie);

            currentStage.setTitle("Food  & Drink");
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

    public void gotoPayment(ActionEvent actionEvent) {
        try{
            new SeatDAO().update();
            new BookingReservationDAO().delete();
            new FoodDrinkDao().reset();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/bookingSucess/bookingSucess.fxml"));
            Parent root = loader.load();
            BookingSuccessController bookingSuccessController = loader.getController();

            Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            bookingSuccessController.setStage(currentStage);

            currentStage.setTitle("Thank You");
            Scene scene = new Scene(root, 1600, 900 );
            // Load the CSS file
            scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());
            currentStage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
