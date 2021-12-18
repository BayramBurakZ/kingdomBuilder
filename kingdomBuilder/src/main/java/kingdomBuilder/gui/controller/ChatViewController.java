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
import kingdomBuilder.network.protocol.Message;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class is used to control every user input on the ChatView. Additionally, it updates its
 * containing data.
 */
public class ChatViewController extends Controller implements Initializable {
    /**
     * Represents the store of the application.
     */
    private Store<KBState> store;
    /**
     * Represents the state for internal use.
     */
    // TODO: remove it?
    private KBState state;
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;

    /**
     * Represents the table for the clients on the server.
     */
    @FXML
    private TableView<ClientDAO> tableview_chat;

    /**
     * Represents the column for the client ids of the clients on the server in the table.
     */
    @FXML
    private TableColumn<ClientDAO, String> column_id;

    /**
     * Represents the column for the client names of the clients on the server.
     */
    @FXML
    private TableColumn<ClientDAO, String> column_name;

    /**
     * Represents the column for the game-ids of the clients on the server.
     */
    @FXML
    private TableColumn<ClientDAO, String> column_gameid;

    /**
     * Represents the tab for the global chat.
     */
    @FXML
    private Tab tab_global;

    /**
     * Represents the tab for the game chat.
     */
    @FXML
    private Tab tab_game;

    /**
     * Represents the textarea used for the input for the chat.
     */
    @FXML
    private TextArea chatview_textarea_chatinput;

    /**
     * Represents the button to send chat messages.
     */
    @FXML
    private Button chatview_button_send;

    /**
     * Represents the textarea used for displaying the globalchat.
     */
    @FXML
    private TextArea textarea_globalchat;

    /**
     * Constructs the ChatViewController.
     * @param store The Application's store to set the field.
     */
    public ChatViewController(Store<KBState> store) {
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
        state = store.getState();

        store.subscribe(newState -> {
            tableview_chat.getItems().setAll(newState.clients.values());
            // Client connection
            if (newState.isConnected && !state.isConnected) {
                onConnect();
            } else if (!newState.isConnected && state.isConnected){
                onDisconnect();
            }
            // TODO: failedToConnect mechanism, multiple output in chatarea 
            // Failed to connect
            if (newState.failedToConnect) {
                textarea_globalchat.appendText("<--- Failed to connect to server --->\n");
            }
            state = newState;
        });

        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_gameid.setCellValueFactory(new PropertyValueFactory<>("gameId"));
    }

    /**
     * Sets the functionality for the Send Button.
     * @param event Contains the data from the event source.
     */
    public void onButtonSendPressed(Event event) {
        chatview_textarea_chatinput.appendText(System.lineSeparator());
        printAndSendMessage();
    }

    /**
     * Sets the functionality for KeyEvents.
     * @param event Contains the data from the event source.
     */
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

    /**
     * Updates the UI elements that are important when the client connects to a server.
     */
    public void onConnect() {
        textarea_globalchat.appendText("<--- You are connected to the server --->\n");

        chatview_textarea_chatinput.setDisable(false);
        chatview_button_send.setDisable(false);
    }

    /**
     * Updates the UI elements that are important when the client disconnects from a server.
     */
    public void onDisconnect() {
        textarea_globalchat.appendText("<--- You are disconnected from the server --->\n");

        chatview_textarea_chatinput.setDisable(true);
        chatview_button_send.setDisable(true);
    }

    /**
     * Updates the ChatView with the specific incoming message correctly.
     * @param chatMsg Contains all information of the incoming chat message.
     */
    public void onMessage(Message chatMsg) {
        int senderID = chatMsg.clientId();
        String senderName = store.getState().clients.get(senderID).getName();

        String chatText;
        List<Integer> receivers = chatMsg.receiverIds();
        String message = chatMsg.message();

        if (receivers.size() == 1 && store.getState().clients.size() > 2)
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

    /**
     * Updates the UI when another client left the server.
     * @param clientId the ID of the client which left the server.
     * @param name the name of the client which left the server.
     * @param gameId the game ID of the client which left the server.
     */
    public void onClientLeft(int clientId, String name, int gameId) {
        textarea_globalchat.appendText("<--- " + name + " left the server. --->");
        textarea_globalchat.appendText(System.lineSeparator());
    }

    /**
     * Updates the UI when another client joined the server.
     * @param clientId the ID of the client which left the server.
     * @param name the name of the client which left the server.
     * @param gameId the game ID of the client which left the server.
     */
    public void onClientJoined(int clientId, String name, int gameId) {
        textarea_globalchat.appendText("<--- " + name + " joined the server. --->");
        textarea_globalchat.appendText(System.lineSeparator());
    }

    /**
     * Updates the UI when this client was kicked from the server.
     */
    public void onYouHaveBeenKicked() {
        textarea_globalchat.appendText("<--- You have been kicked from the server --->\n");
    }

    /**
     * Sends the message from the textarea to the specified clients.
     * When the message starts with "@", the message is only to the following client.
     */
    private void printAndSendMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        String stringToSend = message;
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList<>();
            if (tab_global.isSelected()) {

                // Whisper message
                if (message.startsWith("@")) {
                    String pattern = "\\s"; // TODO: whitespaces pattern for duplicate name "... the second"
                    String[] s = message.split(pattern, 2);
                    for (var c : store.getState().clients.entrySet()) {
                        if (s[0].contains(c.getValue().getName())) {
                            receiverIds.add(c.getKey());
                            break;
                        }
                    }

                    // Empty receivers
                    if (receiverIds.isEmpty()) {
                        textarea_globalchat.appendText("<--- " + s[0] + " is not online --->");
                        textarea_globalchat.appendText(System.lineSeparator());
                        chatview_textarea_chatinput.clear();
                        return;
                    }

                    // Catch whisper message to self
                    String playerName = s[0].substring(1); // TODO: cleanup substring, @-usage, ...
                    if (playerName.equals(state.client.getName())) {
                        textarea_globalchat.appendText("<--- You cannot whisper to yourself --->");
                        textarea_globalchat.appendText(System.lineSeparator());
                        chatview_textarea_chatinput.clear();
                        return;
                    }

                    if (s.length > 1) {
                        message = "You whispered " + s[0] + ": " + s[1];
                    } else {
                        // empty whisper message
                        chatview_textarea_chatinput.clear();
                        return;
                    }
                } else { // Global chat message
                    for (var c : store.getState().clients.entrySet()) {
                        receiverIds.add(c.getKey());
                    }
                    // don't send message to ourselves
                    //receiverIds.remove((Integer) store.getState().client.getId());
                    message = "You: " + message;
                }
            }

            store.dispatch(new ChatSendAction(receiverIds, stringToSend));

            textarea_globalchat.appendText(message);
            textarea_globalchat.appendText(System.lineSeparator());
        }
        chatview_textarea_chatinput.clear();
    }

    /**
     * Sets the MainViewController.
     * @param mainViewController MainViewController with all functions.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
