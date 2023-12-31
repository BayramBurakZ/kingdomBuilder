package kingdomBuilder.gui.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kingdomBuilder.actions.*;
import kingdomBuilder.reducers.ApplicationReducer;
import kingdomBuilder.redux.Store;
import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * This Class controls the MenuViewController with all its functions.
 */
public class MenuViewController extends Controller implements Initializable {


    //region FXML-Imports

    /**
     * Represents the VBox that contains all buttons.
     */
    @FXML
    private VBox menuview_vbox;

    /**
     * Represents the button to start a local game.
     */
    @FXML
    private Button menuview_button_localgame;

    /**
     * Represents the button to play an online game.
     */
    @FXML
    private Button menuview_button_onlinegame;

    /**
     * Represents the button to get to the settings.
     */
    @FXML
    private Button menuview_button_settings;

    /**
     * Represents the button to exit the program.
     */
    @FXML
    private Button menuview_button_exit;

    //endregion FXML-Imports

    /**
     * Sets the store.
     *
     * @param store the store to set.
     */
    public MenuViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     *
     * @param location  the location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuview_vbox.setStyle("""
                -fx-background-image: url(kingdomBuilder/gui/textures/Background.png);
                -fx-background-size: cover;
                """);
    }

    /**
     * Initializes layout arrangement.
     */
    private void setupLayout() {
        menuview_button_localgame.prefWidthProperty().bind(menuview_vbox.prefWidthProperty().multiply(0.5));
        menuview_button_onlinegame.prefWidthProperty().bind(menuview_vbox.prefWidthProperty().multiply(0.5));
        menuview_button_settings.prefWidthProperty().bind(menuview_vbox.prefWidthProperty().multiply(0.5));
        menuview_button_exit.prefWidthProperty().bind(menuview_vbox.prefWidthProperty().multiply(0.5));
    }

    /**
     * Sets the functionality for the LocalGame Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onLocalGameButtonPressed(Event event) {
        store.dispatch(ApplicationReducer.LAUNCH_LOCAL_SERVER, null);
        store.dispatch(ApplicationReducer.CONNECT, new InetSocketAddress("localhost", 6666));
    }

    /**
     * Sets the functionality for the OnlineGame Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onOnlineGameButtonPressed(Event event) {
        sceneLoader.showServerConnectView();
    }

    /**
     * Sets the functionality for the Settings Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onSettingsButtonPressed(Event event) {
        sceneLoader.showSettingsView();
    }

    /**
     * Sets the functionality for the Exit Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onExitButtonPressed(Event event) {
        Stage stage = (Stage) menuview_vbox.getScene().getWindow();
        stage.close();
    }
}