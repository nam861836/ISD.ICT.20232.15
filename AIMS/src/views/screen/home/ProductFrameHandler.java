package views.screen.home;

import common.exception.ProductNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartItem;
import entity.product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Utils;
import views.ProductObserver;
import views.screen.FXMLScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ProductFrameHandler extends FXMLScreenHandler implements ProductObserver {

    private static Logger LOGGER = Utils.getLogger(ProductFrameHandler.class.getName());
    @FXML
    protected ImageView productImage;
    @FXML
    protected Label productTitle;
    @FXML
    protected Label productPrice;
    @FXML
    protected Label productAvail;
    @FXML
    protected Spinner<Integer> spinnerChangeNumber;
    @FXML
    protected Button addToCartBtn;
    @FXML
    private Button deleteBtn;

    private Product product;
    private HomeScreenHandler home;

    public ProductFrameHandler(String screenPath, Product product, HomeScreenHandler home) throws SQLException, IOException {
        super(screenPath);
        this.product = product;
        this.product.registerObserver((ProductObserver) this);
        this.home = home;
        deleteBtn.setOnMouseClicked(e -> {
            try {
                deleteProduct();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        addToCartBtn.setOnMouseClicked(event -> {
            try {
                // TODO : Delegate this to controller
                if (spinnerChangeNumber.getValue() > product.getQuantity()) throw new ProductNotAvailableException();
                Cart cart = Cart.getCart();
                // if product already in cart then we will increase the quantity by 1 instead of create the new cartMedia
                CartItem itemInCart = home.getBaseController().checkItemInCart(product);
                if (itemInCart != null) {
                    itemInCart.setQuantity(itemInCart.getQuantity() + 1);
                } else {
                    CartItem cartItem = new CartItem(product, cart, spinnerChangeNumber.getValue(), product.getPrice());
                    cart.getListItem().add(cartItem);
                }

                // subtract the quantity and redisplay
                product.setQuantity(product.getQuantity() - spinnerChangeNumber.getValue());
                productAvail.setText(String.valueOf(product.getQuantity()));
                home.getNumCartItemLabel().setText(String.valueOf(cart.getTotalItem() + " product"));
                PopupScreen.success("The product " + product.getTitle() + " added to Cart");
            } catch (ProductNotAvailableException exp) {
                try {
                    String message = "Not enough product:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + product.getQuantity();
                    LOGGER.severe(message);
                    PopupScreen.error(message);
                } catch (Exception e) {
                    LOGGER.severe("Cannot add product to cart: ");
                }

            } catch (Exception exp) {
                LOGGER.severe("Cannot add product to cart: ");
                exp.printStackTrace();
            }
        });
        setProductInfo();
    }

    private void deleteProduct() throws SQLException {
//        home.removeProduct(this.product);
        // Delete through controller
        home.getBaseController().deleteProduct(this.product);
    }

    /**
     * @return Product
     */
    public Product getProduct() {
        return product;
    }


    /**
     * @throws SQLException
     */
    private void setProductInfo() throws SQLException {
        // set the cover image of product
        File file = new File(product.getImageURL());
        Image image = new Image(file.toURI().toString());
        productImage.setFitHeight(160);
        productImage.setFitWidth(152);
        productImage.setImage(image);

        productTitle.setText(product.getTitle());
        productPrice.setText(Utils.getCurrencyFormat(product.getPrice()));
        productAvail.setText(Integer.toString(product.getQuantity()));
        spinnerChangeNumber.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
        );

        setImage(productImage, product.getImageURL());
    }

    public void productUpdate() {
        // Update when removing product
        home.updateProductList();
    }

}
