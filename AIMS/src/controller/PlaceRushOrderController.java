package controller;

import entity.order.Order;
import entity.order.OrderItem;
import entity.product.Product;
import entity.shipping.AdditionalInfo;
import entity.shipping.Shipment;
import javassist.bytecode.analysis.Util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * This class controls the flow of place rush order usecase in our AIMS project
 *
 *
 */
public class PlaceRushOrderController extends BaseController {
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());


    /**
     * @param deliveryData
     * @param typeDelivery
     */
    //Functional Cohesion
    //Common Coupling
    public static void validatePlaceRushOrderData(Shipment deliveryData) {
        if (deliveryData.getShipType() == utils.Configs.PLACE_RUSH_ORDER) {
           // validate
        }
    }

    public static int askToPlaceRushOrder(Shipment deliveryInfo, Order order){
        deliveryInfo.setProvince(order.getProvince());

        if (deliveryInfo.checkDeliveryInfo() ) {
            if(order.checkProductAvailable()){
                List<OrderItem> l = order.getListOrderAvaiableItems();
                return l.size();
            }
            else return utils.Configs.ERR_PRODUCT_SUPPORT;
        } 
        return utils.Configs.ERR_DELIVERY_INFO_SUPPORT;
    }

    public static void enterAdditionalData(Shipment shipment, String deliveryInstruction, String deliveryTime, String shipmentDetailString){
        AdditionalInfo addInfo = new AdditionalInfo(deliveryTime, deliveryInstruction);
        shipment.enterAdditonal(addInfo);
        shipment.setShipmentDetail(shipmentDetailString);
    }

}
