package views.screen.login;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import common.exception.LoginFailedException;
import controller.AccountController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.signup.SignupScreenHandler;

public class LoginScreenHandler extends BaseScreenHandler  implements Initializable {
    @FXML
    private Button login;

    @FXML
    private ImageView logo;

    @FXML
    private PasswordField password;

    @FXML
    private Button signup;

    @FXML
    private TextField username;

    public LoginScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("assets/images/Logo.png");
        Image image = new Image(file.toURI().toString());
        setBaseController(new AccountController());
        logo.setImage(image);
        login.setOnMouseClicked(e -> {
            try {
                requestLogin(username.getText(), password.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        signup.setOnMouseClicked(e -> {
            try {
                SignupScreenHandler signupHandler = new SignupScreenHandler(stage, Configs.SIGNUP_PATH);
                signupHandler.setScreenTitle("Sign Up");
                signupHandler.show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }

    public AccountController getBaseController() {
        return accountController;
    }

    public void requestLogin(String username, String password) throws SQLException, IOException {
        try {
            getBaseController().login(username, password);
            homeScreenHandler.show();
        } catch(LoginFailedException e) {
            PopupScreen.error(e.getMessage());
        }
    }
}
