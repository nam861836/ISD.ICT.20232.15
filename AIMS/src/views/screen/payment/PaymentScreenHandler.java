package views.screen.payment;

import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import subsystem.vnPay.Config;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentScreenHandler extends BaseScreenHandler {

    private Invoice invoice;
    @FXML
    private Label pageTitle;
    @FXML
    private VBox vBox;


    public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;

        displayWebView(); // Control Coupling //Control Cohesion

    }

    // Control Coupling // Data Coupling 
    private void displayWebView() {
        var paymentController = new PaymentController();

        var paymentUrl = paymentController.getUrlPay(invoice.getAmount(), "Thanh toan hoa don AIMS");
        WebView paymentView = new WebView();
        WebEngine webEngine = paymentView.getEngine();
        webEngine.load(paymentUrl);
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            // Xử lý khi URL thay đổi
            // Data Coupling // Control Coupling
            handleUrlChanged(newValue);
        });
        vBox.getChildren().clear();
        vBox.getChildren().add(paymentView);
    }

    // Hàm chuyển đổi query string thành Map
//Functional Cohesion
    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    /**
     * Xử lý khi URL thay đổi
     *
     * @param newValue url vnPay return về
     */
// Control Cohesion
    private void handleUrlChanged(String newValue) {
        if (newValue.contains(Config.vnp_ReturnUrl)) {
            try {
                URI uri = new URI(newValue);
                String query = uri.getQuery();

                // Chuyển đổi query thành Map
                Map<String, String> params = parseQueryString(query);

                payOrder(params);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thực hiện thanh toán đơn hàng
     *
     * @param res kết quả vnPay trả về
     * @throws IOException
     */
// Control Coupling
// Control Cohesion
    void payOrder(Map<String, String> res) throws IOException {

        var ctrl = (PaymentController) super.getBaseController();
        Map<String, String> response = ctrl.makePayment(res, this.invoice.getOrder().getId());

        // Tạo và hiển thị màn hình kết quả
        BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH,
                response.get("RESULT"), response.get("MESSAGE"));
        ctrl.emptyCart();
        resultScreen.setPreviousScreen(this);
        resultScreen.setHomeScreenHandler(homeScreenHandler);
        resultScreen.setScreenTitle("Result Screen");
        resultScreen.show();

    }

}
