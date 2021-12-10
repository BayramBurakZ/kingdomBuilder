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
    private ChatViewController chatViewController;
    private Store<KBState> store;
    private boolean connected = false;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store = Store.get();
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
        if(chatViewController == null)
            chatViewController = mainViewController.getSceneLoader().getChatViewController();

        if (connected) {
            // Disconnect from server
            store.dispatch(new ClientDisconnectAction());
            chatViewController.onClientDisconnected();

            menuview_connect_button.setText("Connect");
            menuview_textfield_address.setDisable(false);
            menuview_textfield_port.setDisable(false);

            menuview_localgame_button.setDisable(true);
            menuview_onlinegame_button.setDisable(true);

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
            menuview_connect_button.setText("Disconnect");

            menuview_localgame_button.setDisable(false);
            menuview_onlinegame_button.setDisable(false);

            connected = true;
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}
