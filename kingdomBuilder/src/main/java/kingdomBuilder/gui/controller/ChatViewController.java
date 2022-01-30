package kingdomBuilder.gui.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.chat.ChatSendAction;
import kingdomBuilder.gui.gameboard.TextureLoader;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.Message;
import kingdomBuilder.redux.Store;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.*;

/**
 * This class is used to control every user input on the ChatView. Additionally, it updates its
 * containing data.
 */
public class ChatViewController extends Controller implements Initializable {

    /**
     * Represents a List with Clients to compare with the one in the state so new clients or
     * removed clients can be detected.
     */
    private final List<ClientData> clients = new ArrayList<>();

    //region FXML-Imports

    /**
     * Represents the table for the clients on the server.
     */
    @FXML
    private TableView<ClientData> tableview_chat;

    /**
     * Represents the column for the client ids of the clients on the server in the table.
     */
    @FXML
    private TableColumn<ClientData, String> column_id;

    /**
     * Represents the column for the client names of the clients on the server.
     */
    @FXML
    private TableColumn<ClientData, String> column_name;

    /**
     * Represents the column for the game-ids of the clients on the server.
     */
    @FXML
    private TableColumn<ClientData, String> column_gameid;

    @FXML
    private TabPane tab;

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
     * Represents the tab for the game log.
     */
    @FXML
    private Tab tab_log;

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
     * Represents the webview used for displaying the log.
     */
    @FXML
    private WebView webview_log;

    //endregion FXML-Imports

    /**
     * The body of the html file used for displaying the global chat.
     */
    private Element globalChatBody;

    /**
     * Represents the Gui State, if the client is connected.
     */
    private boolean isConnected;

    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

    /**
     * Constructs the ChatViewController.
     * @param store the Application's store to set the field.
     */
    public ChatViewController(Store<KBState> store) {
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
        resourceBundle = resources;

        // TODO: anywhere where Platform.runLater is called
        //       any code in the GUI should be run by the JavaFX thread
        //       JavaFX thread should therefore be notified by Redux

        store.subscribe(kbState -> {
            tableview_chat.getItems().setAll(kbState.clients.values());
            onClientChanges();
        }, "clients");

        store.subscribe(this::onMessage, "message");

        store.subscribe(state -> {
            if (state.game == null) {
                tab_game.setDisable(true);
                tab_log.setDisable(true);
            } else {
                tab_game.setDisable(false);
                tab_log.setDisable(false);
            }
        }, "game");

        store.subscribe(state -> {

            if (state.isConnected && !isConnected) {
                onConnect();
                isConnected = true;
            } else if (!state.isConnected && isConnected){
                onDisconnect();
                isConnected = false;
            }

            // Failed to connect
            if (state.failedToConnect) {
                // TODO: error message instead of Chat message (because the chat is not visible)
                Platform.runLater(() -> {
                    var elem = createHTMLElement(
                            "<--- " + resourceBundle.getString("failedToConnect") + " --->",
                            MessageStyle.WARNING);
                    globalChatAppendElement(elem);
                });
            }
        }, "isConnected");

        setupClientList();
        setupWebView();
    }

    /**
     * Setup for the table that shows all connected clients.
     */
    //Todo: update the list instead setting it only once
    private void setupClientList() {
        tableview_chat.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        column_id.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().clientId())));
        column_name.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().name())));
        column_gameid.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().gameId())));
    }

    private static String globalStylesheetPath =
            ChatViewController.class.getResource("/kingdomBuilder/gui/chatStylesheets/global.css").toString();
    private static String gameStylesheetPath =
            ChatViewController.class.getResource("/kingdomBuilder/gui/chatStylesheets/game.css").toString();

    WebView webview_globalchat;

    /**
     * Setup for the WebView that displays the Chat-Log.
     */
    private void setupWebView() {

        // create Webview here instead of in the fxml-file because changing content in tabs has weird behavior.
        webview_globalchat = new WebView();
        VBox chatContent = new VBox(webview_globalchat);
        VBox.setVgrow(webview_globalchat, Priority.ALWAYS);

        tab_global.setContent(chatContent);

        webview_globalchat.setContextMenuEnabled(false);
        WebEngine webEngine = webview_globalchat.getEngine();
        webEngine.setUserStyleSheetLocation(globalStylesheetPath);
        webEngine.loadContent("<html><body></body></html>");

        // move chat from game and global tab
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == tab_global) {
                webEngine.setUserStyleSheetLocation(globalStylesheetPath);
                tab_global.setContent(chatContent);
                tab_game.setContent(null);
            } else if (newValue == tab_game) {
                webEngine.setUserStyleSheetLocation(gameStylesheetPath);
                tab_game.setContent(chatContent);
                tab_global.setContent(null);
            }
        });

        webEngine.getLoadWorker().workDoneProperty().addListener((observable, oldValue, newValue) -> {
            globalChatBody = (Element) webEngine.getDocument().getElementsByTagName("body").item(0);
        });

    }

    /**
     * Sets the functionality for the clear selection button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onClearSelectionButtonPressed(Event event) {
        tableview_chat.getSelectionModel().clearSelection();
    }

    /**
     * Sets the functionality for the send button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onSendButtonPressed(Event event) {
        printAndSendMessage();
    }

    /**
     * Sets the functionality for the whisper button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onWhisperButtonPressed(Event event) {
        printAndSendWhisper();
    }

    /**
     * Sets the functionality for KeyEvents.
     * @param event contains the data from the event source.
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
        if (!isConnected) {
            Platform.runLater(() -> {
                var elem = createHTMLElement(
                        "<--- " + resourceBundle.getString("youAreConnected") + " --->",
                        MessageStyle.SERVER);
                globalChatAppendElement(elem);
            });
        }

        chatview_textarea_chatinput.setDisable(false);
        chatview_button_send.setDisable(false);
        chatview_button_whisper.setDisable(false);
    }

    /**
     * Updates the UI elements that are important when the client disconnects from a server.
     */
    public void onDisconnect() {
        if (isConnected) {
            Platform.runLater(() -> {
                var elem = createHTMLElement(
                        "<--- " + resourceBundle.getString("disconnectFromServer") + " --->",
                        MessageStyle.SERVER);
                globalChatAppendElement(elem);
            });
        }

        chatview_textarea_chatinput.setDisable(true);
        chatview_button_send.setDisable(true);
        chatview_button_whisper.setDisable(true);
    }

    /**
     * Updates the ChatView with the specific incoming message correctly.
     */
    public void onMessage(KBState state) {
        Message chatMsg = state.message;
        if (chatMsg == null)
            return;

        int senderID = chatMsg.clientId();
        String senderName = store.getState().clients.get(senderID).name();
        Integer[] receivers = chatMsg.receiverIds().toArray(new Integer[0]);
        String message = chatMsg.message();
        String chatMessage;
        MessageStyle messageStyle;

        if (receivers.length < store.getState().clients.size() - 1) {
            // whisper message
            messageStyle = MessageStyle.WHISPER;
            chatMessage = senderName + " " + resourceBundle.getString("whisperToYou");
            for (int i = 0; i < receivers.length; i++) {
                if (receivers[i].equals(store.getState().client.getClientId())) {
                    continue;
                }

                chatMessage += ", @" + store.getState().clients.get(receivers[i]).name();
            }
            chatMessage += ": ";
        } else {

            if (state.clients.get(chatMsg.clientId()).gameId() == state.client.getGameId() &&
                            state.client.getGameId() != -1) {
                // chat message
                System.out.println("GameChat");
                messageStyle = MessageStyle.GAME_CHAT;
            } else {
                // global message
                messageStyle = MessageStyle.GLOBAL_CHAT;
            }

            chatMessage = senderName + ": ";
        }

        Element msg = createMessage(message, chatMessage, messageStyle);

        globalChatAppendElement(msg);
        //gameChatAppendElement(msg);
    }

    /**
     * Creates an element for the chat.
     * @param messageStyle the style.
     * @return the element.
     */
    private Element createMessage(String text, String sender, MessageStyle messageStyle ) {

        Element senderElement = createHTMLElement(sender, MessageStyle.BOLD);
        Text textElement = createHTMLText(text);

        Element msg = createHTMLElement(messageStyle);
        msg.appendChild(senderElement);
        msg.appendChild(textElement);

        return msg;
    }

    /**
     * Sends a chat message when a client joined the server or left it.
     */
    private void onClientChanges() {
        List<ClientData> clientsState = new ArrayList<ClientData>(store.getState().clients.values());

        List<ClientData> differences = new ArrayList<>();

        if (clientsState.isEmpty()) {
            return;
        }

        if (clientsState.size() > clients.size()) {
            // client joined
            differences.addAll(clientsState);
            differences.removeAll(clients);

            if (differences.get(0).clientId() == store.getState().client.getClientId()) {
                return;
            }
            onClientJoined(differences.get(0));
        } else {
            // client left
            differences.addAll(clients);
            differences.removeAll(clientsState);

            onClientLeft(differences.get(0));
        }

        clients.clear();
        clients.addAll(clientsState);
    }

    /**
     * Sends a chat message when another client left the server.
     * @param clientData the data of the client who left the server.
     */
    private void onClientLeft(ClientData clientData) {
        Platform.runLater(() -> {
            var elem = createHTMLElement(
                    "<--- " + clientData.name() + " " + resourceBundle.getString("leftTheServer") + ". --->",
                    MessageStyle.SERVER);
            globalChatAppendElement(elem);
        });
    }

    /**
     * Sends a chat message when another client joined the server.
     * @param clientData the data of the client who joined the server.
     */
    private void onClientJoined(ClientData clientData) {
        Platform.runLater(() -> {
            var element = createHTMLElement(
                    "<--- " + clientData.name() + " " + resourceBundle.getString("joinedTheServer") + ". --->",
                    MessageStyle.SERVER
            );
            globalChatAppendElement(element);
        });
    }

    /**
     * Updates the UI when this client was kicked from the server.
     */
    public void onYouHaveBeenKicked() {
        Platform.runLater(() -> {
            var element = createHTMLElement(
                    "<--- " + resourceBundle.getString("kickedFromServer") + " --->",
                    MessageStyle.WARNING
            );
            globalChatAppendElement(element);
        });
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
            receiverIds.remove((Integer) store.getState().client.getClientId());

            String senderName = resourceBundle.getString("you").toString() + ": ";
            Element senderElement = createHTMLElement(senderName, MessageStyle.BOLD);
            Text textElement = createHTMLText(message);

            if(tab_global.isSelected()) {
                Element msg = createHTMLElement(MessageStyle.GLOBAL_CHAT);
                msg.appendChild(senderElement);
                msg.appendChild(textElement);
                globalChatAppendElement(msg);
            } else {
                Element msg = createHTMLElement(MessageStyle.GAME_CHAT);
                msg.appendChild(senderElement);
                msg.appendChild(textElement);
                globalChatAppendElement(msg);
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
                    .filtered(clientData -> clientData.clientId() != store.getState().client.getClientId());

            // no receivers selected
            if (receivers.isEmpty()) {
                return;
            }

            // creates message for the chat textarea
            chatMessage = resourceBundle.getString("youWhisper") + " ";
            for (int i = 0; i < receivers.size() - 1; i++) {
                if (receivers.get(i).clientId() == store.getState().client.getClientId()) {
                    continue;
                }
                receiverIds.add(receivers.get(i).clientId());
                chatMessage += "@" + receivers.get(i).name() + ", ";
            }
            receiverIds.add(receivers.get(receivers.size()-1).clientId());
            chatMessage += "@" + receivers.get(receivers.size()-1).name() + ": ";
            Element receiversElement = createHTMLElement(chatMessage, MessageStyle.BOLD);
            Text textElement = createHTMLText(message);

            if (tab_global.isSelected()) {
                Element messageElement = createHTMLElement(MessageStyle.WHISPER);
                messageElement.appendChild(receiversElement);
                messageElement.appendChild(textElement);
                globalChatAppendElement(messageElement);
            } else {
                Element messageElement = createHTMLElement(MessageStyle.WHISPER);
                messageElement.appendChild(receiversElement);
                messageElement.appendChild(textElement);
                globalChatAppendElement(messageElement);
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
        SERVER,

        /**
         * Text should be highlighted as a bold message.
         */
        BOLD
    }

    /**
     * Returns the Document used for the chat log.
     *
     * @return The Document used for the chat log.
     */
    private Document getDocument() {

        WebEngine webEngine = webview_globalchat.getEngine();
        Document doc = webEngine.getDocument();

        if (doc == null) {
            throw new NullPointerException("Document was null!");
        }
        // TODO: maybe return as HTMLDocument instead
        return doc;
    }

    /**
     * Creates the HTML Text object with the specified text content.
     *
     * @param textContent the text content for that element.
     * @return The HTML Text object.
     */
    private Text createHTMLText(String textContent) {

        return getDocument().createTextNode(textContent);
    }

    /**
     * Creates the HTML Element with the specified message highlighting style.
     *
     * @param textContent the text content for that element.
     * @param style the style for highlighting the message.
     * @return The HTML Element.
     */
    private Element createHTMLElement(String textContent, MessageStyle style) {

        Element element = createHTMLElement(style);
        element.setTextContent(textContent);
        return element;
    }

    /**
     * Creates the HTML Element with the specified message highlighting style.
     *
     * @param style the style for highlighting the message.
     * @return The HTML Element wit the highlighting style.
     */
    private Element createHTMLElement(MessageStyle style) {

        return switch (style) {
            case SERVER -> createHTMLElementByTag("server");
            case WARNING -> createHTMLElementByTag("warning");
            case GLOBAL_CHAT -> createHTMLElementByTag("global");
            case GAME_CHAT -> createHTMLElementByTag("game");
            case WHISPER -> createHTMLElementByTag("whisper");
            case BOLD -> createHTMLElementByTag("b");
        };
    }

    /**
     * Creates the HTML Element with the specified message highlighting style.
     *
     * @param tagName the HTML element's tag name.
     * @return HTML Element with the highlighting style.
     */
    private Element createHTMLElementByTag(String tagName) {
        return getDocument().createElement(tagName);
    }

    /**
     * Appends the element with the specified tag name to the global chat html body.
     * @param element the HTML element to be appended to the chat log.
     */
    private void globalChatAppendElement(Element element) {

        if (globalChatBody == null) {
            System.out.println("Global chat body was null while printing: \"" + element.toString() + "\"");
            return;
        }

        WebEngine webEngine = webview_globalchat.getEngine();
        Document doc = getDocument();


        int scrollY = (Integer) webEngine.executeScript("window.scrollY");
        int scrollHeight = (Integer) webEngine.executeScript( "document.documentElement.scrollHeight");
        int clientHeight = (Integer) webEngine.executeScript( "document.body.clientHeight");
        boolean scrollToBottom = scrollY == (scrollHeight - clientHeight);

        globalChatBody.appendChild(element);
        element.appendChild(doc.createElement("br"));

        if (scrollToBottom) {
            webEngine.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
        }
    }
}
