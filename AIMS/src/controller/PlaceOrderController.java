package controller;

import entity.cart.Cart;
import entity.cart.CartItem;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderItem;
import entity.product.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class PlaceOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder
     * button
     *
     * @throws SQLException
     */
    //Coincidental Cohesion
    //Không xác định coupling
    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     *
     * @return Order
     * @throws SQLException
     */

    //Functional Cohesion
    //Control Coupling
    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListItem()) {
            CartItem cartItem = (CartItem) object;
            OrderItem orderItem = new OrderItem(cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getPrice());
            order.getListOrderItem().add(orderItem);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     *
     * @param order
     * @return Invoice
     */

    //Functional Cohesion
    //Control coupling
    public Invoice createInvoice(Order order) {

        order.createOrderEntity();
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */

    //Coincidental Cohesion
    //Data Coupling
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    //Không xác định cohesion
    //Không xác dịnh coupling
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {

    }


    /**
     * @param phoneNumber
     * @return boolean
     */

    //Functional Cohesion
    //Control Coupling
    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 10)
            return false;
        if (Character.compare(phoneNumber.charAt(0), '0') != 0)
            return false;
        try {
            Long.parseUnsignedLong(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


    /**
     * @param name
     * @return boolean
     */

    //Functional Cohesion
    //Control Coupling
    public boolean validateContainLetterAndNoEmpty(String name) {
        // Check name is not null
        if (name == null)
            return false;
        // Check if contain leter space only
        if (name.trim().length() == 0)
            return false;
        // Check if contain only leter and space
        if (name.matches("^[a-zA-Z ]*$") == false)
            return false;
        return true;
    }

    public boolean validateNoEmpty(String name) {
        // Check name is not null
        if (name == null)
            return false;
        // Check if contain leter space only
        if (name.trim().length() == 0)
            return false;
        // Check if contain only leter and space
        return true;
    }

    /**
     * This method calculates the shipping fees of order
     *
     * @param amount
     * @return shippingFee
     */

    //Không xác định cohesion
    //không xác định coupling
    public int calculateShippingFee(int amount) {
        Random rand = new Random();
        int fees = (int) (((rand.nextFloat() * 10) / 100) * amount);
        return fees;
    }

    /**
     * This method get product available place rush order media
     *
     * @param order
     * @return media
     * @throws SQLException
     */

    //Functional Cohesion
    //Control Coupling
    // public Product getProductAvailablePlaceRush(Order order) throws SQLException {
    //     Product product = new Product();
    //     for (OrderItem pd : order.getListOrderItem()) {
    //         // CartItem cartItem = (CartItem) object;
    //         if( validateProductPlaceRushorder()){
    //             product = pd.getProduct();
    //         }
    //     }
    //     return product;
    // }


    /**
     * @param province
     * @param address
     * @return boolean
     */

    //Functional Cohesion
    //Control Coupling
    public boolean validateAddressPlaceRushOrder(String province, String address) {
        if (!validateContainLetterAndNoEmpty(address))
            return false;
        if (!province.equals("Hà Nội"))
            return false;
        return true;
    }


    /**
     * @return boolean
     */

    //Functional Cohesion
    //Control Coupling
    // public boolean validateProductPlaceRushorder() {
    //     if (Product.getIsSupportedPlaceRushOrder())
    //         return true;
    //     return false;
    // }
}
