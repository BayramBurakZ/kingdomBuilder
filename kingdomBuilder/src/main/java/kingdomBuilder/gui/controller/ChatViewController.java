package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.KBState;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.server.ClientJoined;
import kingdomBuilder.network.protocol.server.ClientLeft;
import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ChatViewController extends Controller implements Initializable {
    private String playerName;
    private Store<KBState> store;
    private Client client;
    private MainViewController mainViewController;
    private Consumer<ClientJoined> subOnClientJoined;
    private Consumer<ClientLeft> subOnClientLeft;
    private Consumer<Message> subOnMessage;

    @FXML
    private TableView<ClientDAO> tableview_chat;

    @FXML
    private TableColumn<ClientDAO, String> column_id;

    @FXML
    private TableColumn<ClientDAO, String> column_name;

    @FXML
    private TableColumn<ClientDAO, String> column_gameid;

    @FXML
    private Tab tab_global;

    @FXML
    private Tab tab_game;

    @FXML
    private TextArea chatview_textarea_chatinput;

    @FXML
    private TextArea textarea_globalchat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        store = Store.get();

        store.subscribe(kbState -> {
            tableview_chat.getItems().setAll(kbState.clients.values());
        });

        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_gameid.setCellValueFactory(new PropertyValueFactory<>("gameid"));
    }

    public void onButtonSendPressed(Event event) {
        chatview_textarea_chatinput.appendText(System.lineSeparator());
        printAndSendMessage();
    }

    public void onEnterPressed(KeyEvent event) {
        if (event.isShiftDown() && event.getCode().equals(KeyCode.ENTER)) {
            // TODO: linebreak isn't supported in messages through the server protocol?
            System.out.println("Shift linebreak");
            chatview_textarea_chatinput.appendText(System.lineSeparator());
        } else if (event.getCode() == KeyCode.ENTER) {
            printAndSendMessage();
        }
    }

    public void onClientConnected(){
        client = Client.getMain();

        //incoming chat message
        client.onMessage.subscribe(m -> {
            int senderID = m.clientId();
            String senderName = store.getState().clients.get(senderID).getName();

            String chatText;
            Integer[] receivers = m.receiverIds();
            String message = m.message();

            if (receivers.length == 1 && store.getState().clients.size() > 2)
                chatText = senderName + " whispers to you: " + message;
            else {
                //if there are only 2 clients on server and one gets a whisper message
                if (m.message().startsWith("@")) {
                    String pattern = "\\s";
                    String[] s = message.split(pattern, 2);
                    message = s[1];
                }
            }
            chatText = senderName + ": " + message;
            textarea_globalchat.appendText(chatText);
            textarea_globalchat.appendText(System.lineSeparator());
        });

        //incoming message that someone lefted the server
        client.onClientLeft.subscribe(c -> {
            textarea_globalchat.appendText("<--- " + c.name() + " left the server. --->");
            textarea_globalchat.appendText(System.lineSeparator());
        });

        //incoming message that someone joined the server
        client.onClientJoined.subscribe(c -> {
            textarea_globalchat.appendText("<--- " + c.name() + " joined the server. --->");
            textarea_globalchat.appendText(System.lineSeparator());
        });
    }

    public void onClientDisconnected(){
        client.onClientJoined.unsubscribe(subOnClientJoined);
        client.onClientLeft.unsubscribe(subOnClientLeft);
        client.onMessage.unsubscribe(subOnMessage);
    }

    private void printAndSendMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        String stringToSend = message;
        if (!message.isEmpty()) {
            List<Integer> messageReceiver = new ArrayList();
            if(tab_global.isSelected()) {
                if (!message.startsWith("@")) {
                    for (var c : store.getState().clients.entrySet()) {
                        messageReceiver.add(c.getKey());
                    }
                    // don't send message to ourselves
                    messageReceiver.remove((Integer) store.getState().clientID);
                    message = "You: " + message;
                } else {
                    String pattern = "\\s";
                    String[] s = message.split(pattern, 2);
                    for (var c : store.getState().clients.entrySet()) {
                        if (s[0].contains(c.getValue().getName())) {
                            messageReceiver.add(c.getKey());
                            break;
                        }
                    }
                    message = "You whispered " + s[0] + ": " + s[1];
                }
            }

            Client.getMain().chat(stringToSend, messageReceiver);

            System.out.println("Sending this: " + stringToSend);
            textarea_globalchat.appendText(message);
            textarea_globalchat.appendText(System.lineSeparator());
        }
        chatview_textarea_chatinput.clear();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
