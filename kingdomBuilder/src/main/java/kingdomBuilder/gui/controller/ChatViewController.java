package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.ChatSendAction;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatViewController extends Controller implements Initializable {
    private Store<KBState> store;
    private KBState state;
    private MainViewController mainViewController;

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
    private Button chatview_button_send;

    @FXML
    private TextArea textarea_globalchat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store = Store.get();
        state = store.getState();

        store.subscribe(newState -> {
            tableview_chat.getItems().setAll(newState.clients.values());
            // Client connection
            if (newState.isConnected && !state.isConnected) {
                onConnect();
            } else if (!newState.isConnected && state.isConnected){
                onDisconnect();
            }
            state = newState;
        });

        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_gameid.setCellValueFactory(new PropertyValueFactory<>("gameId"));
    }

    public void onButtonSendPressed(Event event) {
        chatview_textarea_chatinput.appendText(System.lineSeparator());
        printAndSendMessage();
    }

    public void onKeyPressed(KeyEvent event) {
        /*
        if (event.isShiftDown() && event.getCode().equals(KeyCode.ENTER)) {
            // TODO: linebreak isn't supported in messages through the server protocol?
            System.out.println("Shift linebreak");
            chatview_textarea_chatinput.appendText(System.lineSeparator());
        } else

         */
        if (event.getCode() == KeyCode.ENTER) {
            printAndSendMessage();
        }
    }

    public void onConnect() {
        textarea_globalchat.appendText("<--- You are connected to the server --->\n");

        chatview_textarea_chatinput.setDisable(false);
        chatview_button_send.setDisable(false);
    }

    public void onDisconnect() {
        textarea_globalchat.appendText("<--- You are disconnected from the server --->\n");

        chatview_textarea_chatinput.setDisable(true);
        chatview_button_send.setDisable(true);
    }

    public void onMessage(Message chatMsg) {
        int senderID = chatMsg.clientId();
        String senderName = store.getState().clients.get(senderID).getName();

        String chatText;
        Integer[] receivers = chatMsg.receiverIds();
        String message = chatMsg.message();

        if (receivers.length == 1 && store.getState().clients.size() > 2)
            chatText = senderName + " whispers to you: " + message;
        else {
            //if there are only 2 clients on server and one gets a whisper message
            if (chatMsg.message().startsWith("@")) {
                String pattern = "\\s";
                String[] s = message.split(pattern, 2);
                message = s[1];
            }
            chatText = senderName + ": " + message;
        }
        textarea_globalchat.appendText(chatText);
        textarea_globalchat.appendText(System.lineSeparator());
    }

    public void onClientLeft(int clientId, String name, int gameId) {
        textarea_globalchat.appendText("<--- " + name + " left the server. --->");
        textarea_globalchat.appendText(System.lineSeparator());
    }

    public void onClientJoined(int clientId, String name, int gameId) {
        textarea_globalchat.appendText("<--- " + name + " joined the server. --->");
        textarea_globalchat.appendText(System.lineSeparator());
    }

    public void onYouHaveBeenKicked() {
        textarea_globalchat.appendText("<--- You have been kicked from the server --->\n");
    }

    private void printAndSendMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        String stringToSend = message;
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList();
            if (tab_global.isSelected()) {
                if (!message.startsWith("@")) {
                    for (var c : store.getState().clients.entrySet()) {
                        receiverIds.add(c.getKey());
                    }
                    // don't send message to ourselves
                    receiverIds.remove((Integer) store.getState().client.getId());
                    message = "You: " + message;
                } else {
                    String pattern = "\\s";
                    String[] s = message.split(pattern, 2);
                    for (var c : store.getState().clients.entrySet()) {
                        if (s[0].contains(c.getValue().getName())) {
                            receiverIds.add(c.getKey());
                            break;
                        }
                    }
                    if (receiverIds.isEmpty()) {
                        textarea_globalchat.appendText("<--- " + s[0] + " is not online. --->");
                        textarea_globalchat.appendText(System.lineSeparator());
                        chatview_textarea_chatinput.clear();
                        return;
                    }
                    message = "You whispered " + s[0] + ": " + s[1];
                }
            }

            store.dispatch(new ChatSendAction(receiverIds, stringToSend));

            textarea_globalchat.appendText(message);
            textarea_globalchat.appendText(System.lineSeparator());
        }
        chatview_textarea_chatinput.clear();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
