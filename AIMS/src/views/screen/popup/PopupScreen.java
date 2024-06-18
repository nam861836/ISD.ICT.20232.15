package views.screen.popup;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.Configs;
import views.screen.BaseScreenHandler;

import java.io.IOException;


public class PopupScreen extends BaseScreenHandler {


    @FXML
    ImageView tickicon;

    @FXML
    Label message;


    public PopupScreen(Stage stage) throws IOException {
        super(stage, Configs.POPUP_PATH);
    }


    /**
     * @param message
     * @param imagepath
     * @param undecorated
     * @return PopupScreen
     * @throws IOException
     */
    private static PopupScreen popup(String message, String imagepath, Boolean undecorated) throws IOException {
        PopupScreen popup = new PopupScreen(new Stage());
        if (undecorated) popup.stage.initStyle(StageStyle.UNDECORATED);
        popup.message.setText(message);
        popup.setImage(imagepath);
        return popup;
    }


    /**
     * @param message
     * @throws IOException
     */
    public static void success(String message) throws IOException {
        popup(message, Configs.IMAGE_PATH + "/" + "tickgreen.png", true).show(true);
    }


    /**
     * @param message
     * @throws IOException
     */
    public static void error(String message) throws IOException {
        popup(message, Configs.IMAGE_PATH + "/" + "tickerror.png", false).show(false);
    }


    /**
     * @param message
     * @return PopupScreen
     * @throws IOException
     */
    public static PopupScreen loading(String message) throws IOException {
        return popup(message, Configs.IMAGE_PATH + "/" + "loading.gif", true);
    }


    /**
     * @param path
     */
    public void setImage(String path) {
        super.setImage(tickicon, path);
    }


    /**
     * @param autoclose
     */
    public void show(Boolean autoclose) {
        super.show();
        if (autoclose) close(0.8);
    }


    /**
     * @param time
     */
    public void show(double time) {
        super.show();
        close(time);
    }


    /**
     * @param time
     */
    public void close(double time) {
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished(event -> stage.close());
        delay.play();
    }
}
