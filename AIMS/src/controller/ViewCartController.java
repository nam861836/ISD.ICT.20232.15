package controller;

import entity.cart.Cart;
import entity.cart.CartItem;

import java.sql.SQLException;

/**
 * This class controls the flow of events when users view the Cart
 *
 *
 */
public class ViewCartController extends BaseController {

    /**
     * This method checks the available products in Cart
     *
     * @throws SQLException
     */

    //Functional Cohesion
    //Control Coupling
    public void checkAvailabilityOfProduct() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method calculates the cart subtotal
     *
     * @return subtotal
     */

    //Không xác định cohesion
    //không xác định coupling
    public int getCartSubtotal() {
        int subtotal = Cart.getCart().calSubtotal();
        return subtotal;
    }

    // Delete product in cart
    public void removeCartItem(CartItem cartItem) {
        Cart.getCart().removeCartItem(cartItem);
    }

}
