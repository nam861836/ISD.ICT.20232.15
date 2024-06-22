package controller;

import entity.cart.Cart;
import entity.cart.CartItem;
import entity.product.Product;

import java.util.List;


/**
 * This class is the base controller for our AIMS project.
 *
 *
 */
public class BaseController {

    /**
     * The method checks whether the Product in Cart, if it were in, we will return
     * the CartItem else return null.
     *
     * @param product product object
     * @return CartItem or null
     */
    //Functional Cohesion
    //Control Coupling
    public CartItem checkItemInCart(Product product) {
        return Cart.getCart().checkItemInCart(product);
    }

    /**
     * This method gets the list of items in cart.
     *
     * @return List[CartItem]
     */
    //Functional Cohesion
    //Không xác đinh coupling
    public List getListCartItem() {
        return Cart.getCart().getListItem();
    }
}
