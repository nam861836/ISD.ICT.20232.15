package views.screen.order;

import entity.order.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import utils.enums.OrderStatus;
import utils.resources.Resource;
import views.screen.FXMLScreenHandler;

import java.io.File;
import java.io.IOException;

public class OrderItemHandler extends FXMLScreenHandler {
    @FXML
    private Label addess;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label createdDate;

    @FXML
    private Label currency;

    @FXML
    private HBox hboxProduct;

    @FXML
    private ImageView image;

    @FXML
    private VBox imageLogoVbox;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label price;

    @FXML
    private Label shippingFees;

    @FXML
    private Label status;

    @FXML
    private VBox vBoxInfo;

    @FXML
    private VBox vBoxPrice;

    @FXML
    private VBox vboxBtns;

    @FXML
    private Button detailOrderBtn;

    private OrderScreenHandler orderItemHandler;
    private Order order;
    public OrderItemHandler(String screenPath, OrderScreenHandler orderScreenHandler) throws IOException {
        super(screenPath);

        this.orderItemHandler = orderScreenHandler;
        detailOrderBtn.setOnMouseClicked(e -> {
            getDetailRequestHandle();
        });

    }

    private void getDetailRequestHandle() {
        Stage newWindow = new Stage();
        try {

            OrderDetailHandler resultScreenHandler = new OrderDetailHandler(newWindow,Configs.ORDER_DETAILS_PATH, order);
            resultScreenHandler.showScreen();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setOrder(Order order) {
        this.order = order;
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        image.setImage(im);
        setOrderInfo();
    }

    private void setOrderInfo(){
        Resource resource = new Resource();
        name.setText("Name: " + this.order.getName());
        addess.setText("Address: " + this.order.getAddress());
        phone.setText("Phone: " + this.order.getPhone());
        price.setText("Price: " + Utils.getCurrencyFormat(this.order.getPaymentTransaction().getAmount() / 1000));
        shippingFees.setText("Shipping fees: " + Utils.getCurrencyFormat(this.order.getShippingFees() ));

        status.setText( "Status: " + resource.orderStatusStringHashMap.get(order.getStatus()));
        createdDate.setText( "Created At: " + Utils.formatDateTime(this.order.getPaymentTransaction().getCreatedAt(), "dd/MM/yyyy HH:mm:ss"));

        displayItemsNeed();

    }
    private void displayItemsNeed(){
        if(this.order.getStatus().ordinal() == OrderStatus.Rejected.ordinal()){
            cancelBtn.setVisible(false);
//            cancelBtn.setManaged(false);
            return;
        }
        cancelBtn.setOnMouseClicked( e -> handleRejectedOrder());
    }
    private void handleRejectedOrder(){

        try {
            orderItemHandler.handleRejectOrder(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
