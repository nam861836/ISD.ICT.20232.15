package entity.cart;

import common.exception.ProductNotAvailableException;
import entity.CartPublisher;
import entity.product.Product;
import views.CartObserver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart implements CartPublisher {
    protected List<CartObserver> cartObservers = new ArrayList<CartObserver>();
    private static Cart cartInstance;
    private List<CartItem> cartItems;


    private Cart() {
        cartItems = new ArrayList<>();
    }

    /**
     * @return Cart
     */
    public static Cart getCart() {
        if (cartInstance == null) cartInstance = new Cart();
        return cartInstance;
    }

    /**
     * @param cartItem
     */
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }


    /**
     * @param cartItem
     */
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        notifyObserver();
    }


    /**
     * @return List
     */
    public List getListItem() {
        return cartItems;
    }

    public void emptyCart() {
        cartItems.clear();
    }


    /**
     * @return int
     */
    public int getTotalItem() {
        int total = 0;
        for (Object obj : cartItems) {
            CartItem cartItem = (CartItem) obj;
            total += cartItem.getQuantity();
        }
        return total;
    }


    /**
     * @return int
     */
    public int calSubtotal() {
        int total = 0;
        for (Object obj : cartItems) {
            CartItem cartItem = (CartItem) obj;
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        return total;
    }


    /**
     * @throws SQLException
     */
    public void checkAvailabilityOfProduct() throws SQLException {
        boolean allAvai = true;
        for (Object object : cartItems) {
            CartItem cartItem = (CartItem) object;
            int requiredQuantity = cartItem.getQuantity();
            int availQuantity = cartItem.getProduct().getQuantity();
            if (requiredQuantity > availQuantity) allAvai = false;
        }
        if (!allAvai) throw new ProductNotAvailableException("Some media not available");
    }


    /**
     * @param product
     * @return CartItem
     */
    public CartItem checkItemInCart(Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId() == product.getId()) return cartItem;
        }
        return null;
    }

    @Override
    public void registerObserver(CartObserver cartObserver) {
        cartObservers.add(cartObserver);
    }

    @Override
    public void unregisterObserver(CartObserver cartObserver) {
        cartObservers.remove(cartObserver);
    }

    @Override
    public void notifyObserver() {
        for (CartObserver cartObserver : cartObservers) {
            cartObserver.cartUpdate();
        }
    }
}
