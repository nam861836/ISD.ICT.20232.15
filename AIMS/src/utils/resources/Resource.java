package utils.resources;

import utils.enums.OrderStatus;

import java.util.HashMap;
import java.util.ResourceBundle;

public class Resource {
    private static final String BUNDLE_NAME = "utils.resources.vn";
    private ResourceBundle resourceBundle;

    public Resource() {
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        initializeOrderStatusStringMap();
    }

    public HashMap<OrderStatus, String> orderStatusStringHashMap = new HashMap<>();

    private void initializeOrderStatusStringMap() {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            String statusString = resourceBundle.getString(orderStatus.name());
            orderStatusStringHashMap.put(orderStatus, statusString);
        }
    }
}
