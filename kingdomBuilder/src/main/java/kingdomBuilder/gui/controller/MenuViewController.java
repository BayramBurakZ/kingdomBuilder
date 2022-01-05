package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Class controls the MenuViewController with all its functions.
 */
public class MenuViewController extends Controller implements Initializable {

    @FXML
    private VBox menuview_vbox;

    @FXML
    private Button menuview_button_localgame;

    @FXML
    private Button menuview_button_onlinegame;

    @FXML
    private Button menuview_button_settings;

    @FXML
    private Button menuview_button_exit;

    /**
     * Sets the store.
     * @param store The store to set.
     */
    public MenuViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setupLayout();
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
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onLocalGameButtonPressed(Event event) {
        sceneLoader.showGameSettingsView(false);
    }

    /**
     * Sets the functionality for the OnlineGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onOnlineGameButtonPressed(Event event) {
        sceneLoader.showServerConnectView();
    }

    /**
     * Sets the functionality for the Settings Button.
     * @param event  Contains the data from the event source.
     */
    @FXML
    private void onSettingsButtonPressed(Event event) {
        sceneLoader.showSettingsView();
    }

    /**
     * Sets the functionality for the Exit Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onExitButtonPressed(Event event) {
        // TODO: general application close mechanism
        Stage stage = (Stage) menuview_vbox.getScene().getWindow();
        stage.close();
    }
}