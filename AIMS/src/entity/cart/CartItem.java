package entity.cart;

import entity.product.Product;

public class CartItem {

    private Product product;
    private int quantity;
    private int price;

    public CartItem() {

    }

    public CartItem(Product product, Cart cart, int quantity, int price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    /**
     * @return Product
     */
    public Product getProduct() {
        return this.product;
    }


    /**
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;
    }


    /**
     * @return int
     */
    public int getQuantity() {
        return this.quantity;
    }


    /**
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * @return int
     */
    public int getPrice() {
        return this.price;
    }


    /**
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }


    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{"
                + " product='" + product + "'"
                + ", quantity='" + quantity + "'"
                + "}";
    }

}
