package views.screen.home;

import common.exception.ViewCartException;
import controller.HomeController;
import controller.OrderController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.product.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.login.LoginScreenHandler;
import views.screen.order.OrderScreenHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class HomeScreenHandler extends BaseScreenHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

    @FXML
    private Label numProductInCart;

    @FXML
    private Button login;

    @FXML
    private Button orderBtn;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView cartImage;

    @FXML
    private VBox vboxProduct1;

    @FXML
    private VBox vboxProduct2;

    @FXML
    private VBox vboxProduct3;

    @FXML
    private HBox hboxProduct;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    @FXML
    private HBox pageNumberHbox;

    // Aggregation of product list
    private List homeProducts;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    private int numberOfProductPerPage = 12;
    private int currentPage;

    /**
     * @return Label
     */
//Functional Cohesion
    public Label getNumCartItemLabel() {
        return this.numProductInCart;
    }

    /**
     * @return HomeController
     */
//Functional Cohesion
    public HomeController getBaseController() {
        return (HomeController) super.getBaseController();
    }

    @Override
//Sequential Cohesion
    public void show() {
        try {
            if (accountController.getLoggedInAccount() != null) {
                login.setText("Chào mừng, " + accountController.getLoggedInAccount().getName());
            }
        } catch(Exception e) {
            System.out.println("null");
        }
        numProductInCart.setText(String.valueOf(Cart.getCart().getListItem().size()) + " product");
        super.show();
    }

    /**
     * @param arg0
     * @param arg1
     */
    @Override
// Control Coupling
// Control Cohesion
    public void initialize(URL arg0, ResourceBundle arg1) {
        numberOfProductPerPage = 12;
        currentPage = 1;
        setBaseController(new HomeController());
        loadScreen();

        login.setOnMouseClicked(e -> {
            LoginScreenHandler loginHandler;
            try {
                loginHandler = new LoginScreenHandler(stage, Configs.LOGIN_PATH);
                loginHandler.setHomeScreenHandler(this);
                loginHandler.setScreenTitle("Login");
                accountController = loginHandler.getBaseController();
                loginHandler.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        aimsImage.setOnMouseClicked(e -> {
            addProductsHomePage(getProductPaged(currentPage));
        });

        cartImage.setOnMouseClicked(e -> {

            try {
                var cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBaseController(new ViewCartController());
                cartScreen.show(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        orderBtn.setOnMouseClicked(e -> {
            try {
                var orderScreen = new OrderScreenHandler(this.stage, Configs.ORDER_PATH);
                orderScreen.setHomeScreenHandler(this);
                orderScreen.setBaseController(new OrderController());
                orderScreen.show(this);
            } catch (IOException | SQLException e1) {
                e1.printStackTrace();
            }
        });

        addProductsHomePage(this.homeProducts);
        addFilterOption(0, "Book", splitMenuBtnSearch);
        addFilterOption(1, "DVD", splitMenuBtnSearch);
        addFilterOption(2, "CD", splitMenuBtnSearch);
    }

    public void loadScreen() {
        try {
            List products = getBaseController().getAllProduct();
            this.homeProducts = new ArrayList<>();
            for (Object object : products) {
                Product product = (Product) object;
                ProductFrameHandler m1 = new ProductFrameHandler(Configs.HOME_MEDIA_PATH, product, this);
                this.homeProducts.add(m1);
            }
        } catch (SQLException | IOException e) {
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }

        int numberOfPage = (int) Math.ceil((double) homeProducts.size() / numberOfProductPerPage);

        for (int i = 0; i < numberOfPage; i++){
            Button button = new Button(String.valueOf(i + 1));
            button.setOnMouseClicked(e -> {
                int pageIndex = Integer.parseInt(button.getText()) - 1;
                List productPaged = getProductPaged(pageIndex);
                addProductsHomePage(productPaged);
                currentPage = pageIndex + 1;
            });
            pageNumberHbox.getChildren().add(button);
        }
    }

    //Data Coupling
//Data Cohesion
    public void setImage() {
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file2 = new File(Configs.IMAGE_PATH + "/" + "cart.png");
        Image img2 = new Image(file2.toURI().toString());
        cartImage.setImage(img2);
    }

    public List getProductPaged(int pageNumber) {
        int startIndex = pageNumber * numberOfProductPerPage;
        int lastIndex = Math.min(startIndex + numberOfProductPerPage, homeProducts.size());
        List productPaged = new ArrayList<>(homeProducts.subList(startIndex, lastIndex));
        return productPaged;
    }

    /**
     * @param products
     */
//Data Coupling 
//Data Cohesion
    public void addProductsHomePage(List products) {
        ArrayList productItems = (ArrayList) ((ArrayList) products).clone();
        hboxProduct.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while (!productItems.isEmpty()) {
            hboxProduct.getChildren().forEach(node -> {
                int vid = hboxProduct.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < 3 && !productItems.isEmpty()) {
                    ProductFrameHandler product = (ProductFrameHandler) productItems.get(0);
                    vBox.getChildren().add(product.getContent());
                    productItems.remove(product);
                }
            });
            return;
        }
    }

    /**
     * @param position
     * @param text
     * @param menuButton
     */

// Data Coupling 
// Data Cohesion
    private void addFilterOption(int position, String text, MenuButton menuButton) {
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            // empty home product
            hboxProduct.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only product with the choosen category
            List filteredProducts = new ArrayList<>();
            homeProducts.forEach(me -> {
                ProductFrameHandler product = (ProductFrameHandler) me;
                if (product.getProduct().getTitle().toLowerCase().startsWith(text.toLowerCase())) {
                    filteredProducts.add(product);
                }
            });

            // fill out the home with filted product as category
            addProductsHomePage(filteredProducts);
        });
        menuButton.getItems().add(position, menuItem);
    }

    public void updateProductList() {
        pageNumberHbox.getChildren().clear();
        loadScreen();
        addProductsHomePage(getProductPaged(currentPage - 1));
    }

}
