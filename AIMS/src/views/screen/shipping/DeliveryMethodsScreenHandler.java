package views.screen.shipping;

import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.shipping.AdditionalInfo;
import entity.shipping.Shipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

import java.io.IOException;

public class DeliveryMethodsScreenHandler extends BaseScreenHandler {

    private Order order;

    @FXML
    private RadioButton placeRushOrderValue;

    @FXML
    private RadioButton placeOrderValue;

    @FXML
    private TextField deliveryInstruction;

    @FXML
    private TextField shipmentDetail;

    @FXML
    private DatePicker deliveryTime;

    @FXML
    private Label errorProvince;

    @FXML
    private Button updateDeliveryMethodInfoButton;

    public DeliveryMethodsScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
        super(stage, screenPath);
        this.order = order;
    }


    // /**
    //  * @param event
    //  * @throws IOException
    //  */
    // @FXML
    // private void updateDeliveryMethodInfo(MouseEvent event) throws IOException {
    //     String deliveryInstructionString = new String(deliveryInstruction.getText());
    //     String shipmentDetailString = new String(shipmentDetail.getText());
    //     String deliveryDateString = new String();
    //     if (deliveryTime.getValue() != null) {
    //         deliveryDateString = new String(deliveryTime.getValue().toString());
    //     }
    //     int typeDelivery;
    //     if (placeRushOrderValue.isSelected()) {
    //         typeDelivery = utils.Configs.PLACE_RUSH_ORDER;
    //     } else {
    //         typeDelivery = utils.Configs.PALCE_ORDER;
    //     }
    //     var shipment = new Shipment(typeDelivery);
    //     shipment.setShipmentDetail(shipmentDetailString);
    //     shipment.setDeliveryTime(deliveryDateString);
    //     shipment.setDeliveryInstruction(deliveryInstructionString);

    //     PlaceRushOrderController.validatePlaceRushOrderData(shipment);
    //     order.setShipment(shipment);

    //     // // create invoice screen
    //     Invoice invoice = getBaseController().createInvoice(order);
    //     BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
    //     InvoiceScreenHandler.setPreviousScreen(this);
    //     InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
    //     InvoiceScreenHandler.setScreenTitle("Invoice Screen");
    //     InvoiceScreenHandler.setBaseController(getBaseController());
    //     InvoiceScreenHandler.show();
    // }

    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void updateDeliveryMethodInfo(MouseEvent event) throws IOException {
        String deliveryInstructionString = new String(deliveryInstruction.getText());
        String shipmentDetailString = new String(shipmentDetail.getText());
        String deliveryDateString = new String();
        
        if (deliveryTime.getValue() != null) {
            deliveryDateString = new String(deliveryTime.getValue().toString());
        }
        int typeDelivery;
        if (placeRushOrderValue.isSelected()) {
            typeDelivery = utils.Configs.PLACE_RUSH_ORDER;
        } else {
            typeDelivery = utils.Configs.PALCE_ORDER;
        }
        var shipment = new Shipment(typeDelivery);
    
        if( typeDelivery == utils.Configs.PLACE_RUSH_ORDER ){
            PlaceRushOrderController.askToPlaceRushOrder(shipment, this.order);
            PlaceRushOrderController.enterAdditionalData(shipment, deliveryInstructionString, deliveryDateString, shipmentDetailString );
        }
       
        order.setShipment(shipment);
        
        // // create invoice screen
        Invoice invoice = getBaseController().createInvoice(order);
        BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
        InvoiceScreenHandler.setPreviousScreen(this);
        InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
        InvoiceScreenHandler.setScreenTitle("Invoice Screen");
        InvoiceScreenHandler.setBaseController(getBaseController());
        InvoiceScreenHandler.show();

    }


    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        // Back to previous screen
        BaseScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH,
                this.order);
        ShippingScreenHandler.setPreviousScreen(this);
        ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
        ShippingScreenHandler.setScreenTitle("Shipping Screen");
        ShippingScreenHandler.setBaseController(getBaseController());
        ShippingScreenHandler.show();
    }


    /**
     * @param event
     */
    @FXML
    private void handleDeliveryType(ActionEvent event) {
        if (placeOrderValue.isSelected()) {
            deliveryInstruction.setDisable(true);
            shipmentDetail.setDisable(true);
            deliveryTime.setDisable(true);
        } else if (placeRushOrderValue.isSelected()) {
            deliveryInstruction.setDisable(false);
            shipmentDetail.setDisable(false);
            deliveryTime.setDisable(false);
        }
        handleRushOrderError(event);
    }


    /**
     * @param event
     */
    @FXML
    private void handleRushOrderError(ActionEvent event) {
        String province = new String(order.getProvince());
        
        errorProvince.setVisible(false);
        deliveryInstruction.setDisable(true);
        shipmentDetail.setDisable(true);
        deliveryTime.setDisable(true);
        updateDeliveryMethodInfoButton.setDisable(false);

        int typeDelivery;
        if (placeRushOrderValue.isSelected()) {
            typeDelivery = utils.Configs.PLACE_RUSH_ORDER;
        } else {
            typeDelivery = utils.Configs.PALCE_ORDER;
        }
        var shipment = new Shipment(typeDelivery);
        shipment.setProvince(province);
        
        int sizeItems = 0;
        if( typeDelivery == utils.Configs.PLACE_RUSH_ORDER )
            sizeItems = PlaceRushOrderController.askToPlaceRushOrder(shipment, this.order);

        // if (!province.equals("Hà Nội")) {
        if ( sizeItems < 0 ){ 
            if (placeRushOrderValue.isSelected()) {
                errorProvince.setVisible(true);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
                updateDeliveryMethodInfoButton.setDisable(true);
            } else {
                updateDeliveryMethodInfoButton.setDisable(false);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
            }
        } else {
            if (placeRushOrderValue.isSelected()) {
                errorProvince.setVisible(false);
                deliveryInstruction.setDisable(false);
                shipmentDetail.setDisable(false);
                deliveryTime.setDisable(false);
                updateDeliveryMethodInfoButton.setDisable(false);
            } else {
                updateDeliveryMethodInfoButton.setDisable(false);
                deliveryInstruction.setDisable(true);
                shipmentDetail.setDisable(true);
                deliveryTime.setDisable(true);
                errorProvince.setVisible(false);
            }
        }
    }

    /**
     * @return PlaceOrderController
     */
    public PlaceOrderController getBaseController() {
        return (PlaceOrderController) super.getBaseController();
    }
}
