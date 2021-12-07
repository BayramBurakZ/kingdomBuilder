package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.ClientRemoveAction;
import kingdomBuilder.actions.SetClientIDAction;
import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Store;
import kingdomBuilder.KBState;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MenuViewController extends Controller implements Initializable {

    private MainViewController mainViewController;
    private Client client;
    private Store<KBState> store;

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


    public void OnConnectButtonPressed() {
        String address = menuview_textfield_address.getText().trim();
        String port = menuview_textfield_port.getText().trim();

        if(address.isEmpty() || port.isEmpty())
            return;

        createClient(address, Integer.parseInt(port));
        menuview_textfield_address.setDisable(true);
        menuview_textfield_port.setDisable(true);
        menuview_connect_button.setText("disconnect");

        //TODO: implement disconnect button and delete next line
        menuview_connect_button.setDisable(true);
    }

    public void createClient(String address, int port) {
        try{
            Client client = new Client(address, port);
            Client.setMain(client);

            var welcomeFut = client.join(store.getState().clientName);

            client.onClientJoined.subscribe(c -> {
                store.dispatch(new ClientAddAction(c));
            });

            client.onClientLeft.subscribe(c -> {
                store.dispatch(new ClientRemoveAction(c));
            });

            // start listening to server with main client
            Thread clientThread = new Thread(client::listen);
            clientThread.start();

            // TODO: move following Block to Client.join()
            // wait for the WelcomeToServer message before proceeding
            try {
                var welcomeToServer = welcomeFut.get();
                store.dispatch(new SetClientIDAction(welcomeToServer.clientId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return;
            }


            System.out.println("Main Client ID: " + store.getState().clientID);

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
            var cv = (ChatViewController)mainViewController.getSceneLoader().getChatViewController();
            cv.onClientConnected();

        } catch ( IOException e){
            //TODO: maybe a popup
            System.out.println("Address not found");
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


}
