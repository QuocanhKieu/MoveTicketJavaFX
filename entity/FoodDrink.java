package moviebookingapp.entity;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class FoodDrink {

    private final int id;
    private String path;
    private String product;
    private int price;
    private int order_quantity;

    private HBox qtyHBox=new HBox();
    private ImageView imageView = new ImageView();

    private String priceString;
    public ImageView getImageView() {
        return imageView;
    }
    public String getPriceString() {
        return this.priceString;
    }

    public FoodDrink(int id, String path, String product, int price, int order_quantity) {
        this.id = id;
        this.path = path;
        this.product = product;
        this.price = price;
        this.order_quantity = order_quantity;
        this.priceString = price +" đ";
//        qtyHBox = new HBox();
        createHbox();
        try {
            // Load the movie image (replace "/path/to/defaultImage.jpg" with a default image path)
            imageView = new ImageView(this.path != null ? this.path : "./moviebookingapp/asset/film_img/11.jpg");;
            imageView.getStyleClass().add("foodDrink-imageView");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createHbox() {
        Button decreBtn = new Button("-");
        decreBtn.getStyleClass().add("decre-btn");
        TextField qtyTf = new TextField(this.order_quantity +"");
        qtyTf.getStyleClass().add("qty-textField");
        Button increBtn = new Button("+");
        increBtn.getStyleClass().add("incre-btn");

        qtyHBox.getStyleClass().add("qty-HBox");
        qtyHBox.getChildren().addAll(decreBtn, qtyTf, increBtn);
    }
    public Button getDecreBtn() {
        return (Button)qtyHBox.getChildren().get(0);
    }
    public TextField getQtyTf() {
        return (TextField)qtyHBox.getChildren().get(1);
    }
    public Button getIncreBtn() {
        return (Button)qtyHBox.getChildren().get(2);
    }
    public HBox getQtyHBox() {
        return qtyHBox;
    }

    public void setQtyHBox(HBox qtyHBox) {
        this.qtyHBox = qtyHBox;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }
}
