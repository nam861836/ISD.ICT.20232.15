package views.screen.cart;

import common.exception.ProductUpdateException;
import entity.cart.CartItem;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Configs;
import utils.Utils;
import views.screen.FXMLScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

// SRP: CartItemHandler xu ly nhieu tac vu cung luc: xu ly giao dien, ket noi database ,..
public class CartItemHandler extends FXMLScreenHandler {

    private static Logger LOGGER = Utils.getLogger(CartItemHandler.class.getName());

    @FXML
    protected HBox hboxProduct;

    @FXML
    protected ImageView image;

    @FXML
    protected VBox description;

    @FXML
    protected Label labelOutOfStock;

    @FXML
    protected VBox spinnerFX;

    @FXML
    protected Label title;

    @FXML
    protected Label price;

    @FXML
    protected Label currency;

    @FXML
    protected Button btnDelete;

    private CartItem cartItem;
    private Spinner<Integer> spinner;
    private CartScreenHandler cartScreen;

    //Data Coupling
    //Functional Cohesion
    public CartItemHandler(String screenPath, CartScreenHandler cartScreen) throws IOException {
        super(screenPath);
        this.cartScreen = cartScreen;
        hboxProduct.setAlignment(Pos.CENTER);
    }

    //Data coupling
    //Functional Cohesion
    /**
     * @param cartItem
     */
    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
        setProductItem();
    }

    //Control coupling
    //Functional Cohesion
    private void setProductItem() {
        title.setText(cartItem.getProduct().getTitle());
        price.setText(Utils.getCurrencyFormat(cartItem.getPrice()));
        File file = new File(cartItem.getProduct().getImageURL());
        Image im = new Image(file.toURI().toString());
        image.setImage(im);
        image.setPreserveRatio(false);
        image.setFitHeight(110);
        image.setFitWidth(92);

        // add delete button
        btnDelete.setFont(Configs.REGULAR_FONT);
        btnDelete.setOnMouseClicked(e -> {
            cartScreen.getBaseController().removeCartItem(cartItem);
        });

        initializeSpinner();
    }


    //Control Coupling
    //Procedural Cohesion
    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, cartItem.getQuantity());
        spinner = new Spinner<Integer>(valueFactory);
        spinner.setOnMouseClicked(e -> {
            try {
                int numOfProd = this.spinner.getValue();
                int remainQuantity = cartItem.getProduct().getQuantity();
                LOGGER.info("NumOfProd: " + numOfProd + " -- remainOfProd: " + remainQuantity);
                if (numOfProd > remainQuantity) {
                    LOGGER.info("product " + cartItem.getProduct().getTitle() + " only remains " + remainQuantity + " (required " + numOfProd + ")");
                    labelOutOfStock.setText("Sorry, Only " + remainQuantity + " remain in stock");
                    spinner.getValueFactory().setValue(remainQuantity);
                    numOfProd = remainQuantity;
                }

                // update quantity of mediaCart in useCart
                cartItem.setQuantity(numOfProd);

                // update the total of mediaCart
                price.setText(Utils.getCurrencyFormat(numOfProd * cartItem.getPrice()));

                // update subtotal and amount of Cart
                cartScreen.updateCartAmount();

            } catch (SQLException e1) {
                throw new ProductUpdateException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }

        });
        spinnerFX.setAlignment(Pos.CENTER);
        spinnerFX.getChildren().add(this.spinner);
    }
}