package kingdomBuilder.gui.controller;

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

public class MenuViewController extends Controller implements Initializable {

    private MainViewController mainViewController;
    private Store<KBState> store;
    private KBState state;

    @FXML
    private BorderPane menuview_boarderpane;

    @FXML
    private Button menuview_localgame_button;

    @FXML
    private Button menuview_onlinegame_button;

    @FXML
    private TextField menuview_textfield_address;

    @FXML
    private TextField menuview_textfield_port;

    @FXML
    private Button menuview_connect_button;

    public MenuViewController(Store<KBState> store) {
        this.store = store;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store.subscribe(newState -> {
            if(state == null) {
                state = newState;
                return;
            }
            // Client connection
            if (newState.isConnected && !state.isConnected) {
                onConnect();
            } else if (!newState.isConnected && state.isConnected){
                onDisconnect();
            }
            state = newState;
        });
    }

    @FXML
    public void onLocalGameButtonPressed(Event event) {
        mainViewController.showGameSelectionView();
    }

    public void onExitButtonPressed() {
        // TODO: general application close mechanism
        Stage stage = (Stage) menuview_boarderpane.getScene().getWindow();
        stage.close();
    }

    //TODO: This is an awful solution to connect/disconnect
    // send a "bye" message to a server and handle this method within an event.
    // delete Client so we can reconnect -> Client state move to store
    // and update Client list on disconnect
    public void OnConnectButtonPressed() {

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

    private void onConnect() {

        menuview_textfield_address.setDisable(true);
        menuview_textfield_port.setDisable(true);
        menuview_connect_button.setText("Disconnect");

        menuview_localgame_button.setDisable(false);
        menuview_onlinegame_button.setDisable(false);
    }

    private void onDisconnect() {
        menuview_textfield_address.setDisable(false);
        menuview_textfield_port.setDisable(false);
        menuview_connect_button.setText("Connect");

        menuview_localgame_button.setDisable(true);
        menuview_onlinegame_button.setDisable(true);
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}