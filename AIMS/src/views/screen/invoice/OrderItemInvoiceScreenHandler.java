package views.screen.invoice;

import entity.order.OrderItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Utils;
import views.screen.FXMLScreenHandler;

import java.io.IOException;
import java.sql.SQLException;

//Khong vi pham quy tac SOLID

public class OrderItemInvoiceScreenHandler extends FXMLScreenHandler {

    @FXML
    private HBox hboxProduct;

    @FXML
    private VBox imageLogoVbox;

    @FXML
    private ImageView image;

    @FXML
    private VBox description;

    @FXML
    private Label title;

    @FXML
    private Label numOfProd;

    @FXML
    private Label labelOutOfStock;

    @FXML
    private Label price;

    private OrderItem orderItem;

    //Data Coupling
    //Functional Cohesion
    public OrderItemInvoiceScreenHandler(String screenPath) throws IOException {
        super(screenPath);
    }

    //Data Coupling
    //Functional Cohesion
    /**
     * @param orderItem
     * @throws SQLException
     */
    public void setOrderItem(OrderItem orderItem) throws SQLException {
        this.orderItem = orderItem;
        setItemInfo();
    }

    //Control Coupling
    //Functional Cohesion
    /**
     * @throws SQLException
     */
    public void setItemInfo() throws SQLException {
        title.setText(orderItem.getProduct().getTitle());
        price.setText(Utils.getCurrencyFormat(orderItem.getPrice()));
        numOfProd.setText(String.valueOf(orderItem.getQuantity()));
        setImage(image, orderItem.getProduct().getImageURL());
        image.setPreserveRatio(false);
        image.setFitHeight(90);
        image.setFitWidth(83);
    }

}
