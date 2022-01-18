package kingdomBuilder.gui.controller;

import javafx.beans.property.BooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kingdomBuilder.actions.*;
import kingdomBuilder.redux.Store;
import kingdomBuilder.KBState;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Class controls the MenuViewController with all its functions.
 */
public class MenuViewController extends Controller implements Initializable {
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;
    /**
     * Represents the store of the application.
     */
    private Store<KBState> store;
    /**
     * Represents the state for internal use.
     */
    private KBState state;

    @FXML
    private BooleanProperty isConnected;

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
     * Constructs the MenuViewController.
     * @param store  The Application's store to set the field.
     */
    public MenuViewController(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store.subscribe(s -> {
            state = s;
            if(s.isConnected) onConnect();
            else              onDisconnect();
        }, "isConnected");

        store.subscribe(s -> {
            menuview_connect_button.setDisable(s.isConnecting);
        }, "isConnecting");
    }

    /**
     * Sets the functionality for the LocalGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onLocalGameButtonPressed(Event event) {
        mainViewController.showGameSelectionView();
    }

    /**
     * Sets the functionality for the Exit Button.
     */
    public void onExitButtonPressed() {
        // TODO: general application close mechanism
        Stage stage = (Stage) menuview_boarderpane.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the functionality for the Connect Button.
     */
    public void OnConnectButtonPressed() {
        //TODO: This is an awful solution to connect/disconnect
        // send a "bye" message to a server and handle this method within an event.
        // delete Client so we can reconnect -> Client state move to store
        // and update Client list on disconnect
        if (state.isConnected) {
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

    /**
     * Sets the MainViewController.
     * @param mainViewController MainViewController with all functions.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}