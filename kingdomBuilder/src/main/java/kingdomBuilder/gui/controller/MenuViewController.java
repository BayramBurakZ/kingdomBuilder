package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kingdomBuilder.actions.*;
import kingdomBuilder.redux.Store;
import kingdomBuilder.KBState;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuViewController extends Controller implements Initializable {

    private MainViewController mainViewController;
    private ChatViewController chatViewController;
    private Store<KBState> store;
    private boolean connected = false;

    @FXML
    private TextField menuview_textfield_address;

    @FXML
    private TextField menuview_textfield_port;

    @FXML
    private Button menuview_connect_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store = Store.get();
    }

    @FXML
    public void onButtonNewGamePressed(Event event) {
        mainViewController.showGameSelectionView();
    }


    //TODO: This is an awful solution to connect/disconnect
    // send a "bye" message to a server and handle this method within an event.
    // delete Client so we can reconnect -> Client state move to store
    // and update Client list on disconnect
    public void OnConnectButtonPressed() {
        if(chatViewController == null)
            chatViewController = mainViewController.getSceneLoader().getChatViewController();

        if (connected) {
            // Disconnect from server
            store.dispatch(new ClientDisconnectAction());
            chatViewController.onClientDisconnected();

            menuview_connect_button.setText("connect");
            menuview_textfield_address.setDisable(false);
            menuview_textfield_port.setDisable(false);
            //menuview_textfield_address.setText("");
            //menuview_textfield_port.setText("");
            connected = false;

        } else {
            // Connect to server
            String address = menuview_textfield_address.getText().trim();
            String port = menuview_textfield_port.getText().trim();

            if (address.isEmpty() || port.isEmpty())
                return;

            store.dispatch(new ClientConnectAction(address, Integer.parseInt(port)));

            // change gui buttons
            menuview_textfield_address.setDisable(true);
            menuview_textfield_port.setDisable(true);
            menuview_connect_button.setText("disconnect");

            connected = true;
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}
