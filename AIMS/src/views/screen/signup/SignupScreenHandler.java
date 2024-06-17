package views.screen.signup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import common.exception.LoginFailedException;
import common.exception.SignupFailedException;
import controller.AccountController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.login.LoginScreenHandler;
import views.screen.popup.PopupScreen;

public class SignupScreenHandler extends BaseScreenHandler  implements Initializable {
    @FXML
    private DatePicker birthdate;

    @FXML
    private Button cancel;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ImageView logo;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button signup;

    @FXML
    private TextField username;

    public SignupScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("assets/images/Logo.png");
        Image image = new Image(file.toURI().toString());

        setBaseController(new AccountController());
        logo.setImage(image);
        cancel.setOnMouseClicked(e -> {
            try {
                LoginScreenHandler loginHandler = new LoginScreenHandler(stage, Configs.LOGIN_PATH);
                loginHandler.setScreenTitle("Login");
                loginHandler.show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        signup.setOnMouseClicked(e -> {
            try {
                if (birthdate.getValue() == null) {
                    PopupScreen.error("Vui lòng điền đầy đủ các trường");
                } else {
                    requestSignup(username.getText(), password.getText(), confirmPassword.getText(), name.getText(), birthdate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), phoneNumber.getText());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }

    public AccountController getBaseController() {
        return accountController;
    }

    public void requestSignup(String username, String password, String confirmPassword, String name, String birthDate, String phoneNumber) throws SQLException, IOException {
        try {
            getBaseController().signup(name, username, password, confirmPassword,  birthDate, phoneNumber);
            PopupScreen.success("Tạo tài khoản thành công");
            HomeScreenHandler homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
            homeHandler.setScreenTitle("Home Screen");
            homeHandler.setImage();
            homeHandler.show();
        } catch(SignupFailedException e) {
            PopupScreen.error(e.getMessage());
        }
    }
}
