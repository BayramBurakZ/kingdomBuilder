package kingdomBuilder.gui.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.ChatSendAction;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Store;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
     * Represents the state for internal use.
     */
    // TODO: remove it?
    private KBState state;

    /**
     * Represents the button to clear the selection.
     */
    @FXML
    private Button chatview_button_clear;

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
     * Represents the button to send a whisper message.
     */
    @FXML
    private Button chatview_button_whisper;

    /**
     * Represents the webview used for displaying the globalchat.
     */
    @FXML
    private WebView webview_globalchat;

    /**
     * The body of the html file used for displaying the global chat.
     */
    private Element globalChatBody;

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
                print("<--- Failed to connect to server --->\n", MessageStyle.WARNING);
            }
            state = newState;
        });

        tableview_chat.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_gameid.setCellValueFactory(new PropertyValueFactory<>("gameId"));

        webview_globalchat.setContextMenuEnabled(false);

        WebEngine webEngine = webview_globalchat.getEngine();
        webEngine.setUserStyleSheetLocation(
                this.getClass().getResource("/kingdomBuilder/gui/chatStylesheets/global.css").toString());
        webEngine.loadContent("<html><body></body></html>");

        webEngine.getLoadWorker().workDoneProperty().addListener((observable, oldValue, newValue) -> {
            globalChatBody = (Element) webEngine.getDocument().getElementsByTagName("body").item(0);
        });
    }

    /**
     * Sets the functionality for the clear selection button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onButtonClearSelectionPressed(Event event) {
        tableview_chat.getSelectionModel().clearSelection();
    }

    /**
     * Sets the functionality for the send button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onButtonSendPressed(Event event) {
        printAndSendMessage();
    }

    /**
     * Sets the functionality for the whisper button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onButtonWhisperPressed(Event event) {
        printAndSendWhisper();
    }

    /**
     * Sets the functionality for KeyEvents.
     * @param event Contains the data from the event source.
     */
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.isShiftDown()) {
                printAndSendWhisper();
            } else {
                printAndSendMessage();
            }
        }
    }

    /**
     * Updates the UI elements that are important when the client connects to a server.
     */
    public void onConnect() {
        print("<--- You are connected to the server --->", MessageStyle.SERVER);

        chatview_textarea_chatinput.setDisable(false);
        chatview_button_send.setDisable(false);
        chatview_button_whisper.setDisable(false);
    }

    /**
     * Updates the UI elements that are important when the client disconnects from a server.
     */
    public void onDisconnect() {
        print("<--- You are disconnected from the server --->", MessageStyle.SERVER);

        chatview_textarea_chatinput.setDisable(true);
        chatview_button_send.setDisable(true);
        chatview_button_whisper.setDisable(true);
    }

    /**
     * Updates the ChatView with the specific incoming message correctly.
     * @param chatMsg Contains all information of the incoming chat message.
     */
    public void onMessage(Message chatMsg) {
        int senderID = chatMsg.clientId();
        String senderName = store.getState().clients.get(senderID).getName();
        Integer[] receivers = chatMsg.receiverIds();
        String message = chatMsg.message();
        String chatMessage;

        if (receivers.length < store.getState().clients.size()) {
            // whisper message
            chatMessage = senderName + " whispers to you";
            for (int i = 0; i < receivers.length; i++) {
                if (receivers[i].equals(state.client.getId())) {
                    continue;
                }

                chatMessage += ", @" + state.clients.get(receivers[i]).getName();
            }
            chatMessage += ": " + message;
        } else {
            // global message
            chatMessage = senderName + ": " + message;
        }

        // TODO: check if all receiver IDs match our game's client IDs, then only print in game channel
        print(chatMessage);
    }

    /**
     * Updates the UI when another client left the server.
     * @param clientId the ID of the client which left the server.
     * @param name the name of the client which left the server.
     * @param gameId the game ID of the client which left the server.
     */
    public void onClientLeft(int clientId, String name, int gameId) {
        print("<--- " + name + " left the server. --->", MessageStyle.SERVER);
    }

    /**
     * Updates the UI when another client joined the server.
     * @param clientId the ID of the client which left the server.
     * @param name the name of the client which left the server.
     * @param gameId the game ID of the client which left the server.
     */
    public void onClientJoined(int clientId, String name, int gameId) {
        print("<--- " + name + " joined the server. --->", MessageStyle.SERVER);
    }

    /**
     * Updates the UI when this client was kicked from the server.
     */
    public void onYouHaveBeenKicked() {
        print("<--- You have been kicked from the server --->", MessageStyle.WARNING);
    }

    /**
     * Sends the message from the textarea to all clients in the current channel.
     */
    private void printAndSendMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        String chatMessage = message;
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList<>();

            for (var c : store.getState().clients.entrySet()) {
                receiverIds.add(c.getKey());
            }
            // don't send message to ourselves
            receiverIds.remove((Integer) store.getState().client.getId());

            message = "You: " + message;

            if(tab_global.isSelected()) {
                print(message, MessageStyle.GLOBAL_CHAT);
            } else {
                // TODO: text output for game chat
            }

            store.dispatch(new ChatSendAction(receiverIds, chatMessage));
        }
        chatview_textarea_chatinput.clear();
    }

    /**
     * Sends the message from the textarea to the selected clients.
     */
    private void printAndSendWhisper() {
        String message = chatview_textarea_chatinput.getText().trim();
        String chatMessage = "";
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList<>();

            // don't send message to ourselves
            var receivers = tableview_chat.getSelectionModel().getSelectedItems()
                    .filtered(clientDAO -> clientDAO.getId() != state.client.getId());

            // no receivers selected
            if (receivers.isEmpty()) {
                return;
            }

            // creates message for the chat textarea
            chatMessage = "You whispered ";
            for (int i = 0; i < receivers.size() - 1; i++) {
                if (receivers.get(i).getId() == state.client.getId()) {
                    continue;
                }
                receiverIds.add(receivers.get(i).getId());
                chatMessage += "@" + receivers.get(i).getName() + ", ";
            }
            receiverIds.add(receivers.get(receivers.size()-1).getId());
            chatMessage += "@" + receivers.get(receivers.size()-1).getName() + ": " + message;

            if (tab_global.isSelected()) {
                print(chatMessage, MessageStyle.WHISPER);
            } else {
                // text output for game chat
            }

            store.dispatch(new ChatSendAction(receiverIds, message));
        }
        chatview_textarea_chatinput.clear();
    }

    /**
     * Defines the message highlighting style.
     */
    private enum MessageStyle {
        /**
         * Text should be highlighted as a global chat message.
         */
        GLOBAL_CHAT,

        /**
         * Text should be highlighted as a game chat message.
         */
        GAME_CHAT,

        /**
         * Text should be highlighted as a whisper message.
         */
        WHISPER,

        /**
         * Text should be highlighted as a warning message.
         */
        WARNING,

        /**
         * Text should be highlighted as a regular server message.
         */
        SERVER
    }

    /**
     * Prints the message to the chat as a regular server message.
     * @param message The message to print.
     */
    private void print(String message) {
        print(message, MessageStyle.SERVER);
    }

    /**
     * Prints the message to the chat in the specified message highlighting style.
     * @param message The message to print.
     * @param style The style for highlighting the message.
     */
    private void print(String message, MessageStyle style) {

        switch(style) {
            case SERVER -> globalChatAddElement(message, "server");
            case WARNING -> globalChatAddElement(message, "warning");
            case GLOBAL_CHAT -> globalChatAddElement(message, "global");
            case GAME_CHAT -> globalChatAddElement(message,"game");
            case WHISPER -> globalChatAddElement(message,"whisper");
        }
    }

    /**
     * Appends the element with the specified tag name to the global chat html body.
     * @param message The text content of the element.
     * @param tagName The tag/type of the element.
     */
    private void globalChatAddElement(String message, String tagName) {

        if (globalChatBody == null) {
            System.out.println("Global chat body was null!");
            return;
        }

        WebEngine webEngine = webview_globalchat.getEngine();
        Document doc = webEngine.getDocument();

        if (doc == null) {
            System.out.println("Document was null!");
            return;
        }

        Platform.runLater(() -> {
            int scrollY = (Integer) webEngine.executeScript("window.scrollY");
            int scrollHeight = (Integer) webEngine.executeScript( "document.documentElement.scrollHeight");
            int clientHeight = (Integer) webEngine.executeScript( "document.body.clientHeight");
            boolean scrollToBottom = scrollY == (scrollHeight - clientHeight);
            System.out.println("scrollY: "+ scrollY + ", scrollHeight: " + scrollHeight + ", clientHeight: " + clientHeight);

            Element elem = doc.createElement(tagName);
            elem.setTextContent(message);
            globalChatBody.appendChild(elem);
            elem.appendChild(doc.createElement("br"));

            if (scrollToBottom) {
                webEngine.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
            }
        });
    }
}
