package moviebookingapp.controllers.showtimeseat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import moviebookingapp.controllers.FoodDrinkController;
import moviebookingapp.controllers.LoginFormController;
import moviebookingapp.controllers.MovieListController;
import moviebookingapp.dao.*;
import moviebookingapp.database.Connector;
import moviebookingapp.entity.BookingReservation;
import moviebookingapp.entity.Movie;
import moviebookingapp.entity.Seat;
import moviebookingapp.entity.ShowTime;
import moviebookingapp.report.Report;
import moviebookingapp.singleton.ReservationManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShowtimeseatController implements Initializable {

    private final int ADULT_PRICE = 115000;
    private final int CHILD_PRICE = 100000;
    private int total_price = 0;
    public Text totalPrice;
    public Text ticketQtyText;
    public Text staffName;
    public ScrollPane scrollPane;
    public RadioButton childRadio;
    public RadioButton adultRadio;
    public ToggleGroup userType;
    public Button foodDrinkBtn;
    public Hyperlink backBtn;
    public ToggleButton lightMode;
    public ToggleButton darkMode;
    public Button logOutBtn;
    public Pane rootPane;
    public GridPane gridPaneFSeat;
    public ScrollPane bookedReservationPane;
    private Movie movie;
    private Stage currentStage;
    int col;
    int row;
    int ticketQty = 0;
    ArrayList<ShowTime> showtimeList;
    List<Seat> seat_list;
    List<GridPane> reservationGridPaneList;
    public void setMovieInfo(Movie choosedMovie, Stage currentStage ) {
        
        movie = choosedMovie;
        this.currentStage = currentStage;


        totalPrice.setText("0 đ");
        try {
//re-render the reservation side when get back to the scene START
            reservationGridPaneList = new ArrayList<>();
            List<BookingReservation> bookingReservationList = new BookingReservationDAO().list();
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
                    column1.setPrefWidth(180);
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


            VBox re_content_1= new VBox();
            re_content_1.getChildren().addAll(reservationGridPaneList);
            bookedReservationPane.setContent(re_content_1);
//re-render the reservation side when get back to the scene END

// showTiem side START
            showtimeList = new ShowTimeDAO().list(movie);
            VBox content = new VBox(); // for left side - showtime
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                content.getStyleClass().add("content-showtime");
            for (ShowTime showtime : showtimeList) {
//                ShowTime  currentShow = showtime;
                GridPane gridpane = createShowTimeGridPane(showtime);
                gridpane.setId(showtime.getShowtime_id() + "");
                gridpane.getStyleClass().add("showTime-gridpane");
                // styling block
                {
    //                gridpane.setGridLinesVisible(true);
                    ColumnConstraints column1 = new ColumnConstraints();
                    column1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                    column1.setMinWidth(10);
                    column1.setPrefWidth(90);
                    column1.setMaxWidth(Double.MAX_VALUE);

                    ColumnConstraints column2 = new ColumnConstraints();
                    column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                    column2.setMinWidth(10);
                    column2.setPrefWidth(180);
                    column2.setMaxWidth(Double.MAX_VALUE);

                    // Add ColumnConstraints to the GridPane
                    gridpane.getColumnConstraints().addAll(column1, column2);
                }// styling block

//                gridpane.setOnMouseClicked(event-> {
//                    System.out.println(showtimeList.indexOf(currentShow));
//                    List<Seat> seat_list = new SeatDAO().list(showtime.getShowtime_id());
//
//                    gridPaneFSeat.getChildren().clear();
//                    gridPaneFSeat.setGridLinesVisible(true);
////                    char[] alphabet = "ABCDE".toCharArray(); // A, B, C, D, E
//
//                    if(showtime.getTotalSeatQty() != 0) {
//                        int count = 0;
//                        for (row = 0; row < 5; row++) {
//                            for (col = 0; col < 10; col++) {
//                                Seat curent_seat = seat_list.get(count);
//                                int current_col = col;// quantrtong keep track of all the state of all the variables outside the nearest block{} containing the handler
//                                int current_row = row;
//
////                                Button button = new Button(alphabet[row]+ "" + (col + 1));
//                                Button button = new Button();
//                                button.getStyleClass().add("seat-button");
//                                button.setStyle("-fx-background-color:  #D9D9D9;");
//
//                                button.setId(curent_seat.getSeat_id()+"");//setId for each Btn = each seat Id
//
//                                if(curent_seat.getAvailability_status() == 0) {
//                                    button.setStyle("-fx-background-color:  #ff0000;");
//                                    button.setText("X");
//                                    button.getStyleClass().add("seat-button-booked");
//                                    button.setDisable(true);
//                                }
//
//                                if(curent_seat.getAvailability_status() == 1 || curent_seat.getAvailability_status() == 2) {
//                                    if(row<2){
//
//                                        button.setUserData("#D9D9D9");// lưu màu ban đầu để sau đó dùng lại
//                                        button.setStyle("-fx-background-color:  #D9D9D9;");
//                                    }
//                                    else if(row == 4){
//                                        button.setUserData("#FFC0CBFF");// lưu màu ban đầu để sau đó dùng lại
//                                        button.setStyle("-fx-background-color:  #FFC0CBFF;");
//                                        button.getStyleClass().add("seat-button-couple");
//                                    }
//                                    else{
//                                        button.setUserData("#FFFF00CC");// lưu màu ban đầu để sau đó dùng lại
//                                        button.setStyle("-fx-background-color:  #FFFF00CC;");
//                                    }
//                                    button.setDisable(false);
//                                }
//                                if(curent_seat.getAvailability_status() == 2) {
//                                    button.setStyle("-fx-background-color:  #00b5ef;");
//                                    button.setDisable(false);
//
//                                }
//
//                                button.setOnAction((e) -> {
//                                    System.out.println(current_col);
//
////                                    System.out.println(current_col);
////                                    System.out.println(current_row);
//                                    System.out.println(col);
////                                    System.out.println(row);
//                                    if(curent_seat.getAvailability_status() == 1) {
//                                        button.setStyle("-fx-background-color:  #00b5ef;");
//                                        curent_seat.setAvailability_status(2);
//                                        new SeatDAO().update(curent_seat);
//
//                                        int ticket_price = 0;
//
//
//                                        if(adultRadio.isSelected()) {
//                                            if(current_row < 2){
//                                                ticket_price = ADULT_PRICE;
//                                            }else if (current_row == 4) {
//                                                ticket_price = ADULT_PRICE*2;
//                                            }else {
//                                                ticket_price = ADULT_PRICE+50000;
//                                            }
//                                            total_price += ticket_price;
//                                        }else {
//                                            if(current_row < 2){
//                                                ticket_price = CHILD_PRICE;
//                                            }else if (current_row == 4) {
//                                                ticket_price = CHILD_PRICE*2;
//                                            }else {
//                                                ticket_price = CHILD_PRICE+50000;
//                                            }
//                                            total_price += ticket_price;
//                                        }
//                                        totalPrice.setText(total_price+" đ");
//                                        ticketQty +=1;
//                                        ticketQtyText.setText(ticketQty+" ticket(s)");
//
//                                        addBookingAndPane(curent_seat, showtime, reservationGridPaneList,ticket_price);
//
//
//                                    }
//                                    else if(curent_seat.getAvailability_status() == 2) { // dùng else if để nó chỉ chạy 1 block thay vì cả 2 dấn tới ko có j thay đổi
//                                        button.setStyle("-fx-background-color:" + button.getUserData().toString()+";");//set color to the initial seat color
//                                        curent_seat.setAvailability_status(1);
//                                        new SeatDAO().update(curent_seat);
//
//                                        int booking_amount = new BookingReservationDAO().findOne(curent_seat.getSeat_id()).getTicket_price();
//                                            total_price -= booking_amount;
//                                        totalPrice.setText(total_price+" đ");
//                                        ticketQty -=1;
//                                        ticketQtyText.setText(ticketQty+" ticket(s)");
//                                        removeBookingAndPane(curent_seat, reservationGridPaneList);
//
//                                    }
//                                });
//                                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Button takes full cell size
//                                button.setAlignment(Pos.CENTER);
//                                button.setTextAlignment(TextAlignment.CENTER);
//
//                                gridPaneFSeat.setRowIndex(button, row);
//                                gridPaneFSeat.setColumnIndex(button, col);
//                                gridPaneFSeat.getChildren().add(button);
//
//                                count++;
//                            }
//
//                        }
//
//
//                    }
//                });
                content.getChildren().add(gridpane);// đầu tiên tạo list of showTime gridPane
            }

            for (Node node : content.getChildren()) {
                ShowTime  currentShow = showtimeList.get(content.getChildren().indexOf(node));

                node.setOnMouseClicked(event-> {
                    // styling start
                    node.setStyle("-fx-background-color: lightblue;");

                    // Reset styles of other panes
                    for (Node otherNode : content.getChildren()) {
                        if (otherNode != node) {
                            otherNode.setStyle("-fx-background-color: transparent;");
                        }
                    }
                    // styling end

                    seat_list = new SeatDAO().list(currentShow.getShowtime_id());

                    gridPaneFSeat.getChildren().clear();// reset to an empty gridPane

                    if(currentShow.getTotalSeatQty() != 0) {

                        int count = 0;
                        for (row = 0; row < 5; row++) {
                            for (col = 0; col < 10; col++) {
                                Seat curent_seat = seat_list.get(count);
                                int current_col = col;// quantrtong keep track of all the state of all the variables outside the nearest block{} containing the handler
                                int current_row = row;

//                                Button button = new Button(alphabet[row]+ "" + (col + 1));
                                Button button = new Button();
                                button.getStyleClass().add("seat-button");
                                button.setStyle("-fx-background-color:  #D9D9D9;");

                                button.setId(curent_seat.getSeat_id()+"");//setId for each Btn = each seat Id

                                if(curent_seat.getAvailability_status() == 0) {
                                    button.setStyle("-fx-background-color:  #ff0000;");
                                    button.setText("X");
                                    button.getStyleClass().add("seat-button-booked");
                                    button.setDisable(true);
                                }

                                if(curent_seat.getAvailability_status() == 1 || curent_seat.getAvailability_status() == 2) {
                                    if(row<2){

                                        button.setUserData("#D9D9D9");// lưu màu ban đầu để sau đó dùng lại
                                        button.setStyle("-fx-background-color:  #D9D9D9;");
                                    }
                                    else if(row == 4){
                                        button.setUserData("#FFC0CBFF");// lưu màu ban đầu để sau đó dùng lại
                                        button.setStyle("-fx-background-color:  #FFC0CBFF;");
                                        button.getStyleClass().add("seat-button-couple");
                                    }
                                    else{
                                        button.setUserData("#FFFF00CC");// lưu màu ban đầu để sau đó dùng lại
                                        button.setStyle("-fx-background-color:  #FFFF00CC;");
                                    }
                                    button.setDisable(false);
                                }
                                if(curent_seat.getAvailability_status() == 2) {
                                    button.setStyle("-fx-background-color:  #00b5ef;");
                                    button.setDisable(false);

                                }

                                button.setOnAction((e) -> {
                                    System.out.println(current_col);

//                                    System.out.println(current_col);
//                                    System.out.println(current_row);
                                    System.out.println(col);
//                                    System.out.println(row);
                                    if(curent_seat.getAvailability_status() == 1) {
                                        button.setStyle("-fx-background-color:  #00b5ef;");
                                        curent_seat.setAvailability_status(2);
                                        new SeatDAO().update(curent_seat);

                                        int ticket_price = 0;


                                        if(adultRadio.isSelected()) {
                                            if(current_row < 2){
                                                ticket_price = ADULT_PRICE;
                                            }else if (current_row == 4) {
                                                ticket_price = ADULT_PRICE*2;
                                            }else {
                                                ticket_price = ADULT_PRICE+50000;
                                            }
                                            total_price += ticket_price;
                                        }else {
                                            if(current_row < 2){
                                                ticket_price = CHILD_PRICE;
                                            }else if (current_row == 4) {
                                                ticket_price = CHILD_PRICE*2;
                                            }else {
                                                ticket_price = CHILD_PRICE+50000;
                                            }
                                            total_price += ticket_price;
                                        }
                                        totalPrice.setText(total_price+" đ");
                                        ticketQty +=1;
                                        ticketQtyText.setText(ticketQty+" ticket(s)");

                                        addBookingAndPane(curent_seat, currentShow, reservationGridPaneList,ticket_price);


                                    }
                                    else if(curent_seat.getAvailability_status() == 2) { // dùng else if để nó chỉ chạy 1 block thay vì cả 2 dấn tới ko có j thay đổi
                                        System.out.println(curent_seat.getSeat_id());
                                        button.setStyle("-fx-background-color:" + button.getUserData().toString()+";");//set color to the initial seat color
                                        curent_seat.setAvailability_status(1);
                                        new SeatDAO().update(curent_seat);

                                        int booking_amount = new BookingReservationDAO().findOne(curent_seat.getSeat_id()).getTicket_price();
                                        total_price -= booking_amount;
                                        totalPrice.setText(total_price+" đ");
                                        ticketQty -=1;
                                        ticketQtyText.setText(ticketQty+" ticket(s)");
                                        removeBookingAndPane(curent_seat, reservationGridPaneList);
                                    }
                                });
                                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Button takes full cell size
                                button.setAlignment(Pos.CENTER);
                                button.setTextAlignment(TextAlignment.CENTER);
                                if(row != 4) {
                                    gridPaneFSeat.setRowIndex(button, row);
                                    gridPaneFSeat.setColumnIndex(button, col);
                                }
                                else{// row ==4
                                    if(col < 5) {
                                        gridPaneFSeat.setRowIndex(button, row);
                                        gridPaneFSeat.setColumnIndex(button, 2*col+1);

                                    }
                                    else if(col == 5) {
                                        // do nothing skip rendering the  seat
                                        gridPaneFSeat.setRowIndex(button, row+1);
                                        gridPaneFSeat.setColumnIndex(button, 2*col-10);
                                        button.setVisible(false);
                                        button.setDisable(true);
                                    }
                                    else{
                                        gridPaneFSeat.setRowIndex(button, row+1);
                                        gridPaneFSeat.setColumnIndex(button, 2*col-10);
                                    }
                                }
                                gridPaneFSeat.getChildren().add(button);


                                count++;
                            }

                        }


                    }
                });

            }
            scrollPane.setContent(content);
            // showTiem side END

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookedReservationPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        staffName.setText(LoginFormController.activeUser.getUsername());
    }

    public GridPane createShowTimeGridPane(ShowTime showtime) {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                Label label = new Label();
                label.getStyleClass().add("show-label");
                label.getStyleClass().add("show-label" + row + col);

                if(col == 0 && row == 0){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm\ndd/MM");
                    label.setText(showtime.getStart_time().format(formatter));
                }
                if(col == 1 && row == 0){
                    label.setText(movie.getTittle());
                }
                if(col == 0 && row == 1){
                    label.setText("Cinema"+ showtime.getTheater_id());
                }
                if(col == 1 && row == 1){
                    label.setText(showtime.getValidSeatQty()+"/"+showtime.getTotalSeatQty());
                }
                GridPane.setRowIndex(label, row);
                GridPane.setColumnIndex(label, col);
                gridPane.getChildren().add(label);
            }
        }
        return gridPane;
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
                if(col == 1 && row == 2){
                    Button deleteRevBtn  = new Button("Delete");
                    deleteRevBtn.getStyleClass().add("delete-rev-btn");

                    deleteRevBtn.setOnAction(e -> {
                        System.out.println( re.getSeat_id());
                        ObservableList<Node> buttonLs = gridPaneFSeat.getChildren();
                        int buttonExist = 0;
                        for (Node node : buttonLs) {
                            if (node instanceof Button) {
                                Button button = (Button) node;

                                // Check conditions to identify the specific button you want to modify
                                if (Integer.parseInt(button.getId()) == re.getSeat_id()) {
                                    buttonExist = 1;
                                    // Modify the appearance of the specific button
                                    button.setStyle("-fx-background-color:" + button.getUserData().toString()+";");


                                    Seat current_seat = null;
                                    for(Seat seat : seat_list) {
                                        if(seat.getSeat_id() == re.getSeat_id()) {
                                            current_seat = seat;
                                        }
                                    }
                                    if(current_seat == null) try {
                                        throw new Exception("current_seat is null");
                                    } catch (Exception ex) {
                                        System.out.println(ex.toString());
                                    }
                                    current_seat.setAvailability_status(1);
                                    new SeatDAO().update(current_seat);

                                    int booking_amount = new BookingReservationDAO().findOne(current_seat.getSeat_id()).getTicket_price();
                                    total_price -= booking_amount;
                                    totalPrice.setText(total_price+" đ");
                                    ticketQty -=1;
                                    ticketQtyText.setText(ticketQty+" ticket(s)");
                                    removeBookingAndPane(current_seat, reservationGridPaneList);

                                    break;
                                }
                            }
                        }
                        if(buttonExist == 0) {
                             // để thay đổi status của seat mà ko có button seat tương ứng khi click vào 1 showtime khác
                                Seat seat = new SeatDAO().findOne(re.getSeat_id());
                                System.out.println("get here");
                                seat.setAvailability_status(1);
                                new SeatDAO().update(seat);
                                int booking_amount = new BookingReservationDAO().findOne(seat.getSeat_id()).getTicket_price();
                                total_price -= booking_amount;
                                totalPrice.setText(total_price+" đ");
                                ticketQty -=1;
                                ticketQtyText.setText(ticketQty+" ticket(s)");

                                removeBookingAndPane(seat, reservationGridPaneList);
                        }

                        // do sth slike click on blue button
                    });

                    GridPane.setRowIndex(deleteRevBtn, row);
                    GridPane.setColumnIndex(deleteRevBtn, col);
                    gridPane.getChildren().add(deleteRevBtn);


                    break;
                }
                GridPane.setRowIndex(label, row);
                GridPane.setColumnIndex(label, col);
                gridPane.getChildren().add(label);
            }
        }
        return gridPane;
    }
    public void removeBookingAndPane(Seat seat, List<GridPane> list) {
        int seat_id = seat.getSeat_id();
        new BookingReservationDAO().delete((seat_id));

        try {
            list.remove(list.stream().filter((item) -> {
                if(item.getId().equals(seat_id+"")) return true;
                else return false;
            } ).findFirst().orElseThrow(() -> new IllegalStateException("GridPane not present")));//lấy gridPane ra khỏi Optional<GridPane> list
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox booking_content= new VBox();
        booking_content.getChildren().addAll(list);
        bookedReservationPane.setContent(booking_content);
    }
    public void addBookingAndPane(Seat seat, ShowTime showtime, List<GridPane> list, int ticket_price) {
        BookingReservation re = new BookingReservation(
                movie.getId(),
                movie.getTittle(),
                seat.getSeat_id(),
                seat.getSeat_name(),
                adultRadio.isSelected() ? adultRadio.getText() : childRadio.getText(),
                ticket_price,
                showtime.getTheater_id(),
                showtime.getShowtime_id(),
                LocalDate.now()
        );
        new BookingReservationDAO().create(re);// add booking to the db table

        GridPane grid_pane = createReGridPane(re);
        grid_pane.getStyleClass().add("rev-gridPane");
        grid_pane.setId(seat.getSeat_id() + "");// quan trong để remove

        {
            //                gridpane.setGridLinesVisible(true);
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            column1.setMinWidth(10);
            column1.setPrefWidth(180);
            column1.setMaxWidth(Double.MAX_VALUE);

            ColumnConstraints column2 = new ColumnConstraints();
            column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            column2.setMinWidth(10);
            column2.setPrefWidth(160);
            column2.setMaxWidth(Double.MAX_VALUE);

            // Add ColumnConstraints to the GridPane
            grid_pane.getColumnConstraints().addAll(column1, column2);
        }// styling block

        int isExist = 0;
        for (GridPane item : list) {
            if (item.getId().equals(grid_pane.getId())) {
                isExist = 1;
                break;
            }
        }
        if (isExist == 0) list.add(grid_pane);

        VBox booking_content = new VBox();
        booking_content.getChildren().addAll(list);
        bookedReservationPane.setContent(booking_content);
    }
    public void gotoFoodDrink (ActionEvent event) {
        try{
//            new SeatDAO().update();
//            new BookingReservationDAO().delete();
//            new FoodDrinkDao().reset();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/food&drink.fxml"));
            Parent root = loader.load();
            FoodDrinkController foodDrinkController = loader.getController();

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            foodDrinkController.setStage(currentStage, movie);

            currentStage.setTitle("Food & Drink");
            Scene scene = new Scene(root, 1600, 900 );
            // Load the CSS file
            scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());
            currentStage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setLightMode(ActionEvent actionEvent) {
    }
    public void setDarkMode(ActionEvent actionEvent) {
    }
    public void backToMovieList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/moviecinema.fxml"));
            Parent root = loader.load();
            currentStage.setTitle("Movies Cinema");
            Scene scene = new Scene(root, 1600, 900 );
            // Load the CSS file
            scene.getStylesheets().add(getClass().getResource("/moviebookingapp/style.css").toExternalForm());
            currentStage.setScene(scene);
            MovieListController movieListController = loader.getController();
            movieListController.setStage(currentStage);
            currentStage.show();

        } catch (Exception e) {
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
