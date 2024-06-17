package views.screen;

import controller.AccountController;
import controller.BaseController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.screen.home.HomeScreenHandler;

import java.io.IOException;
import java.util.Hashtable;

public class BaseScreenHandler extends FXMLScreenHandler {

    protected final Stage stage;
    protected HomeScreenHandler homeScreenHandler;
    protected Hashtable<String, String> messages;
    protected AccountController accountController;
    private Scene scene;
    private BaseScreenHandler prev;
    private BaseController baseController;

    //Data Coupling
    private BaseScreenHandler(String screenPath) throws IOException {
        super(screenPath);
        this.stage = new Stage();
        accountController = AccountController.getAccountController();
    }


    public BaseScreenHandler(Stage stage, String screenPath) throws IOException {
        super(screenPath);
        this.stage = stage;
        accountController = AccountController.getAccountController();
    }

    /**
     * @return BaseScreenHandler
     */
    public BaseScreenHandler getPreviousScreen() {
        return this.prev;
    }

    /**
     * @param prev
     */
    public void setPreviousScreen(BaseScreenHandler prev) {
        this.prev = prev;
    }

    public void show() {
        if (this.scene == null) {
            this.scene = new Scene(this.content);
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }


    /**
     * @param string
     */
    public void setScreenTitle(String string) {
        this.stage.setTitle(string);
    }

    /**
     * @return BaseController
     */
    public BaseController getBaseController() {
        return this.baseController;
    }

    /**
     * @param baseController
     */
    public void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }

    public void setBaseController(AccountController accountController) {
        this.accountController = accountController;
    }


    /**
     * @param messages
     */
    public void forward(Hashtable messages) {
        this.messages = messages;
    }


    /**
     * @param HomeScreenHandler
     */
    public void setHomeScreenHandler(HomeScreenHandler HomeScreenHandler) {
        this.homeScreenHandler = HomeScreenHandler;
    }

}
