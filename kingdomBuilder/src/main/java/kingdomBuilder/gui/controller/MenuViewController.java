package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kingdomBuilder.actions.*;
import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Class controls the MenuViewController with all its functions.
 */
public class MenuViewController extends Controller implements Initializable {
    /**
     * Represents the BorderPane of the View.
     */
    @FXML
    private BorderPane menuview_boarderpane;

    /**
     * Reprepresents the button for local games.
     */
    @FXML
    private Button menuview_localgame_button;

    /**
     * Represents the button for online games.
     */
    @FXML
    private Button menuview_onlinegame_button;

    /**
     * Represents the TextField for the server address.
     */
    @FXML
    private TextField menuview_textfield_address;

    /**
     * Represents the TextField for the server port.
     */
    @FXML
    private TextField menuview_textfield_port;

    /**
     * Represents the Button to Connect to a server.
     */
    @FXML
    private Button menuview_connect_button;

    /**
     * Represents the Gui State, if the client is connected.
     */
    private boolean isConnected;

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
        if(store.getState().isConnected) {
            isConnected = true;
            onConnect();
        } else {
            isConnected = false;
            onDisconnect();
        }

        store.subscribe(state -> {
            if (state.isConnected && !isConnected) {
                onConnect();
                isConnected = true;
            } else if (!state.isConnected && isConnected){
                onDisconnect();
                isConnected = false;
            }
        });
    }

    /**
     * Sets the functionality for the LocalGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onLocalGameButtonPressed(Event event) {
        sceneLoader.showGameSelectionView();
    }

    /**
     * Sets the functionality for the Settings Button.
     * @param event  Contains the data from the event source.
     */
    @FXML
    private void OnSettingsButtonPressed(Event event) {
        sceneLoader.showSettingsView();
    }

    /**
     * Sets the functionality for the Exit Button.
     */
    @FXML
    private void onExitButtonPressed() {
        // TODO: general application close mechanism
        Stage stage = (Stage) menuview_boarderpane.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the functionality for the Connect Button.
     */
    @FXML
    private void OnConnectButtonPressed() {
        //TODO: This is an awful solution to connect/disconnect
        // send a "bye" message to a server and handle this method within an event.
        // delete Client so we can reconnect -> Client state move to store
        // and update Client list on disconnect
        if (store.getState().isConnected) {
            // Disconnect from server
            store.dispatch(new DisconnectAction());
        } else {
            // Connect to server
            String address = menuview_textfield_address.getText().trim();
            String port = menuview_textfield_port.getText().trim();

            if (address.isEmpty() || port.isEmpty())
                return;

            // TODO: handle failed connection
            store.dispatch(new ConnectAction(address, Integer.parseInt(port)));
        }
    }

    /**
     * Updates the UI when the client connects to a server.
     */
    private void onConnect() {

        menuview_textfield_address.setDisable(true);
        menuview_textfield_port.setDisable(true);
        menuview_connect_button.setText("Disconnect");

        menuview_localgame_button.setDisable(false);
        menuview_onlinegame_button.setDisable(false);
    }

    /**
     * Updates the UI when the client disconnects from a server.
     */
    private void onDisconnect() {
        menuview_textfield_address.setDisable(false);
        menuview_textfield_port.setDisable(false);
        menuview_connect_button.setText("Connect");

        menuview_localgame_button.setDisable(true);
        menuview_onlinegame_button.setDisable(true);
    }
}