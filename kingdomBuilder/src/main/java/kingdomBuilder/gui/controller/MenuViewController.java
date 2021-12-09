package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.ClientRemoveAction;
import kingdomBuilder.actions.SetClientAction;
import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Store;
import kingdomBuilder.KBState;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MenuViewController extends Controller implements Initializable {

    private MainViewController mainViewController;
    private ChatViewController chatViewController;
    private Client client;
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


    //TODO: This is an aweful solution to connect/disconnect
    // send a "bye" message to a server and handle this method within an event.
    // delete Client so we can reconnect -> Client state move to store
    // and update Client list on disconnect
    public void OnConnectButtonPressed() {
        if(chatViewController == null)
            chatViewController = (ChatViewController) mainViewController.getSceneLoader().getChatViewController();

        if (connected) {
            // Disconnect to server
            client.closeSocket();
            chatViewController.onClientDisconnected();

            menuview_connect_button.setText("connect");
            menuview_textfield_address.setDisable(false);
            menuview_textfield_port.setDisable(false);
            menuview_textfield_address.setText("");
            menuview_textfield_port.setText("");
            connected = false;

        } else {
            // Connect to server
            String address = menuview_textfield_address.getText().trim();
            String port = menuview_textfield_port.getText().trim();

            if (address.isEmpty() || port.isEmpty())
                return;

            createClient(address, Integer.parseInt(port));

            // change gui buttons
            menuview_textfield_address.setDisable(true);
            menuview_textfield_port.setDisable(true);
            menuview_connect_button.setText("disconnect");

            connected = true;
        }
    }

    public void createClient(String address, int port) {
        try {
            client = new Client(address, port);

            // start listening to server with main client
            Thread clientThread = new Thread(client::listen);
            clientThread.start();

            var welcomeFut = client.join(store.getState().clientPreferredName);

            // wait for the WelcomeToServer message before proceeding
            try {
                var welcomeToServer = welcomeFut.get();
                store.dispatch(new SetClientAction(client));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return;
            }

            client.onClientJoined.subscribe(c -> {
                store.dispatch(new ClientAddAction(c));
            });

            client.onClientLeft.subscribe(c -> {
                store.dispatch(new ClientRemoveAction(c));
            });

            System.out.println("Main Client ID: " + client.getId());

            // wait to receive all clients from server
            var clientsFut = client.requestClients();
            try {
                var clients = clientsFut.get().clients();
                System.out.println(clients);
                if (clients != null) {
                    for (var c : clients) {
                        store.dispatch(new ClientAddAction(c));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //TODO: event
            chatViewController.onClientConnected();

        } catch (IOException e) {
            //TODO: maybe a popup
            System.out.println("Address not found");
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}
