package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import kingdomBuilder.KBState;
import kingdomBuilder.reducers.ApplicationReducer;
import kingdomBuilder.redux.Store;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for a Server Connection.
 */
public class ServerConnectViewController extends Controller implements Initializable {
    /**
     * Represents the TextField for the server address.
     */
    @FXML
    private TextField textfield_address;

    /**
     * Represents the TextField for the server address.
     */
    @FXML
    private TextField textfield_port;

    /**
     * Constructs the ServerConnectViewController.
     * @param store the Application's store to set the field.
     */
    public ServerConnectViewController(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location the location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: Substates!
        store.subscribe(state -> {
            if (state.isConnected()) {
                System.out.println("Changes to GameSelection");
                sceneLoader.showGameSelectionView();
            }
        }, "isConnected");
    }

    /**
     * Sets the functionality for the Connect Button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onConnectButtonPressed(Event event) {
        // Connect to server
        String address = textfield_address.getText().trim();
        String port = textfield_port.getText().trim();

        if (address.isEmpty() || port.isEmpty())
            return;

        store.dispatch(ApplicationReducer.CONNECT, new InetSocketAddress(address, Integer.parseInt(port)));
    }

    /**
     * Sets the functionality for the Exit Button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onBackToMenuButtonPressed(Event event) {
        sceneLoader.showMenuView();
    }
}
