package kingdomBuilder.gui.controller;

import javafx.beans.property.SimpleStringProperty;
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
import kingdomBuilder.gamelogic.ServerTurn;
import kingdomBuilder.network.protocol.*;
import kingdomBuilder.reducers.ChatReducer;
import kingdomBuilder.redux.Store;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.net.URL;
import java.util.*;

/**
 * This class is used to control every user input on the ChatView. Additionally, it updates its
 * containing data.
 */
public class ChatViewController extends Controller implements Initializable {

    /**
     * Represents the path to the global.css file.
     */
    private final static String globalStylesheetPath = String.valueOf(
            ChatViewController.class.getResource("/kingdomBuilder/gui/chatStylesheets/global.css"));

    /**
     * Represents the path to the game.css file.
     */
    private final static String gameStylesheetPath = String.valueOf(
            ChatViewController.class.getResource("/kingdomBuilder/gui/chatStylesheets/game.css"));

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

    /**
     * Represents the TabPane.
     */
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
     * Represents the WebView used by the chat. Both global and game chat.
     */
    private WebView chatWebView;

    /**
     * The body of the html file used for displaying the global chat.
     */
    private Element globalChatBody;

    /**
     * The body of the html file used for displaying the turn log.
     */
    private Element turnLogBody;

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

        store.subscribe(this::onClientChanges, "clients");
        store.subscribe(this::onMessageChanged, "message");
        store.subscribe(this::onTurn, "gameLastTurn");
        store.subscribe(this::onJoinedGameChanged, "joinedGame");
        store.subscribe(this::onConnect, "isConnected");
        store.subscribe(this::onTurnStartChanged, "nextTerrainCard");
        store.subscribe(this::onQuadrantsUploadedChanged, "quadrantUploaded");
        store.subscribe(this::onScoresChanged, "scores");

        setupClientList();
        setupWebView();
    }

    /**
     * Setup for the table that shows all connected clients.
     */
    private void setupClientList() {
        tableview_chat.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        column_id.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().clientId())));
        column_name.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().name())));
        column_gameid.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().gameId())));

        // highlight own client in table
        tableview_chat.setRowFactory(param -> new TableRow<>() {
            @Override
            protected void updateItem(ClientData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.clientId() == store.getState().client().getClientId()) {
                    setStyle("-fx-background-color: #ff9966;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    /**
     * Setup for the WebView that displays the Chat-Log and Turn-Log.
     */
    private void setupWebView() {

        // Setup for chat log /////////////////////////
        // create Webview here instead of in the fxml-file because changing content in tabs has weird behavior.
        chatWebView = new WebView();
        VBox chatContent = new VBox(chatWebView);
        VBox.setVgrow(chatWebView, Priority.ALWAYS);

        tab_global.setContent(chatContent);

        chatWebView.setContextMenuEnabled(false);
        WebEngine chatWebEngine = chatWebView.getEngine();
        chatWebEngine.setUserStyleSheetLocation(globalStylesheetPath);
        chatWebEngine.loadContent("<html><body></body></html>");

        // move chat from game and global tab
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == tab_global) {
                chatWebEngine.setUserStyleSheetLocation(globalStylesheetPath);
                tab_global.setContent(chatContent);
                tab_game.setContent(null);
            } else if (newValue == tab_game) {
                chatWebEngine.setUserStyleSheetLocation(gameStylesheetPath);
                tab_game.setContent(chatContent);
                tab_global.setContent(null);
            }
        });

        chatWebEngine.getLoadWorker().workDoneProperty().addListener(observable ->
            globalChatBody = (Element) chatWebEngine.getDocument().getElementsByTagName("body").item(0));

        // Setup for turn log /////////////////////////
        webview_log.setContextMenuEnabled(false);
        WebEngine turnLogWebEngine = webview_log.getEngine();
        // TODO: stylesheet for turn log
        turnLogWebEngine.setUserStyleSheetLocation(globalStylesheetPath);
        turnLogWebEngine.loadContent("<html><body></body></html>");

        turnLogWebEngine.getLoadWorker().workDoneProperty().addListener(observable ->
            turnLogBody = (Element) turnLogWebEngine.getDocument().getElementsByTagName("body").item(0));
    }

    /**
     * Sends chat message when the data structure for the quadrants has changed.
     * @param kbState the current state.
     */
    private void onQuadrantsUploadedChanged(KBState kbState) {
        if (kbState.quadrants() != null && kbState.quadrants().size() > 5) {

            QuadrantUploaded m = kbState.quadrantUploaded();
            String clientName = kbState.clients().get(m.clientId()).name();

            String chatMessage = clientName + " " + resourceBundle.getString("hasUploadedNewQuadrant") +
                    " " + m.quadrantId();

            globalChatAppendElement(
                    createMessage(MessageStyle.SERVER,
                            createHTMLElement(chatMessage, null)
                    ));
        }
    }

    /**
     * Prints the scores for each player in the Log.
     * @param kbState the current State.
     */
    private void onScoresChanged(KBState kbState) {
        if (kbState.scores() == null) return;

        //get the score and sort it
        ArrayList<ScoresData> scores = new ArrayList<>(kbState.scores().scoresDataList());
        scores.sort(Comparator.comparing(ScoresData::score).reversed());

        //print the score into LOG
        for (int i = 0; i < scores.size(); i++) {
            ScoresData x = scores.get(i);
            String clientName = kbState.clients().get(x.clientId()).name();
            String chatMessage = i+1 + ". " + clientName;

            turnLogAppendElement(
                    //whisper because its orange - no further reason
                    createMessage(MessageStyle.WHISPER,
                            createHTMLElement(chatMessage, null)
                    ));
        }
    }

    /**
     * Updates the GUI when the client (dis-)connects to a server.
     * @param state the current state.
     */
    private void onConnect(KBState state) {
        if (state.isConnected() && !isConnected) {
            onConnect();
            isConnected = true;
        } else if (!state.isConnected() && isConnected){
            onDisconnect();
            isConnected = false;
        }

        // Failed to connect
        if (state.failedToConnect()) {
            // TODO: error message instead of Chat message (because the chat is not visible)
            var elem = createHTMLElement(
                    "<--- " + resourceBundle.getString("failedToConnect") + " --->",
                    MessageStyle.WARNING);
            globalChatAppendElement(elem);
        }
    }

    /**
     * Updates the UI elements that are important when the client connects to a server.
     */
    private void onConnect() {
        if (!isConnected) {
            var elem = createHTMLElement(
                    "<--- " + resourceBundle.getString("youAreConnected") + " --->",
                    MessageStyle.SERVER);
            globalChatAppendElement(elem);
        }

        chatview_textarea_chatinput.setDisable(false);
        chatview_button_send.setDisable(false);
        chatview_button_whisper.setDisable(false);
    }

    /**
     * Updates the UI elements that are important when the client disconnects from a server.
     */
    private void onDisconnect() {
        if (isConnected) {
            var elem = createHTMLElement(
                    "<--- " + resourceBundle.getString("disconnectFromServer") + " --->",
                    MessageStyle.SERVER);
            globalChatAppendElement(elem);
        }

        chatview_textarea_chatinput.setDisable(true);
        chatview_button_send.setDisable(true);
        chatview_button_whisper.setDisable(true);
    }

    /**
     * Updates the ChatView with the specific incoming message.
     * @param state the current state.
     */
    private void onMessageChanged(KBState state) {
        Message chatMsg = state.message();
        if (chatMsg == null)
            return;

        //TODO: namings
        int senderID = chatMsg.clientId();
        String senderName = store.getState().clients().get(senderID).name();
        Integer[] receivers = chatMsg.receiverIds().toArray(new Integer[0]);
        String message = chatMsg.message();
        String chatMessage;
        MessageStyle messageStyle;

        if (receivers.length < store.getState().clients().size() - 1) {
            // whisper message
            messageStyle = MessageStyle.WHISPER;
            chatMessage = senderName + " " + resourceBundle.getString("whisperToYou");
            for (Integer receiver : receivers) {
                if (receiver.equals(store.getState().client().getClientId())) {
                    continue;
                }

                chatMessage += ", @" + store.getState().clients().get(receiver).name();
            }
            chatMessage += ": ";
        } else {

            if (state.clients().get(chatMsg.clientId()).gameId() == state.client().getGameId() &&
                            state.client().getGameId() != -1) {
                // chat message
                messageStyle = MessageStyle.GAME_CHAT;
            } else {
                // global message
                messageStyle = MessageStyle.GLOBAL_CHAT;
            }

            chatMessage = senderName + ": ";
        }

        globalChatAppendElement(
                createMessage(messageStyle,
                createHTMLElement(chatMessage, MessageStyle.BOLD),
                createHTMLElement(message, null)));
    }

    /**
     * Updates the ChatView with the start of the next turn of the current game.
     * @param state the current state.
     */
    private void onTurnStartChanged(KBState state) {
        if (state.gameStarted()) {
            // game probably has the old player at this point lol
            var player = state.playersMap().get(state.nextPlayer());
            String context = " started their turn: ";
            String terrainName = player.getTerrainCard().name();

            turnLogAppendElement(createMessage(MessageStyle.GAME_CHAT,
                    createHTMLElement(player.name, MessageStyle.BOLD),
                    createHTMLElement(context, null),
                    createHTMLElement(terrainName, MessageStyle.BOLD)));
        }
    }

    /**
     * Updates the ChatView when joining a game.
     * @param kbState the current state.
     */
    private void onJoinedGameChanged(KBState kbState) {
        if (kbState.joinedGame()) {
            tab_game.setDisable(false);
            tab_log.setDisable(false);
        } else {
            tab_game.setDisable(true);
            //tab_log.setDisable(true);
        }
    }

    /**
     * Updates the ChatView with the settlement placement or movement of the current game.
     * @param state the current state.
     */
    private void onTurn(KBState state) {
        if (state.gameLastTurn() instanceof ServerTurn a) {
            String textContent =  a.type == ServerTurn.TurnType.PLACE ?
                    " has placed a settlement at (" + a.x + "," + a.y + ")" :
                    " has moved a settlement from (" + a.x + "," + a.y + ") to (" + a.toX + "," + a.toY + ")";

            turnLogAppendElement(createMessage(MessageStyle.GAME_CHAT,
                    createHTMLElement(state.currentPlayer().name, MessageStyle.BOLD),
                    createHTMLElement(textContent, null)
            ));
        }
    }

    /**
     * Sends a chat message when a client joined the server or left it.
     */
    private void onClientChanges(KBState state) {
        // update client list ///////////////////////
        tableview_chat.getItems().setAll(state.clients().values());

        //send join/left-message ///////////////////
        List<ClientData> clientsState = new ArrayList<>(store.getState().clients().values());

        List<ClientData> differences = new ArrayList<>();

        if (clientsState.isEmpty()) {
            return;
        }

        if (clientsState.size() > clients.size()) {
            // client joined
            differences.addAll(clientsState);
            differences.removeAll(clients);

            if (differences.get(0).clientId() == store.getState().client().getClientId()) {
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
        String text = "<--- " + clientData.name() + " " + resourceBundle.getString("leftTheServer") + ". --->";
        globalChatAppendElement(createHTMLElement(text, MessageStyle.SERVER));
    }

    /**
     * Sends a chat message when another client joined the server.
     * @param clientData the data of the client who joined the server.
     */
    private void onClientJoined(ClientData clientData) {
        String text = "<--- " + clientData.name() + " " + resourceBundle.getString("joinedTheServer") + ". --->";
        globalChatAppendElement(createHTMLElement(text, MessageStyle.SERVER));
    }

    /**
     * Updates the UI when this client was kicked from the server.
     */
    public void onYouHaveBeenKicked() {
        String text = "<--- " + resourceBundle.getString("kickedFromServer") + " --->";
        globalChatAppendElement(createHTMLElement(text, MessageStyle.SERVER));
    }

    /**
     * Sends the message from the textarea to all clients in the current channel.
     */
    private void printAndSendMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        final String chatMessage = message;
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList<>();

            if (tab_global.isSelected()) {
                for (var c : store.getState().clients().entrySet()) {
                    receiverIds.add(c.getKey());
                }
            } else {
                for (var c : store.getState().clients().entrySet()) {
                    if (c.getValue().gameId() == store.getState().client().getGameId())
                        receiverIds.add(c.getKey());
                }
            }

            // don't send message to ourselves
            receiverIds.remove((Integer) store.getState().client().getClientId());

            String senderName = resourceBundle.getString("you") + ": ";

            if(tab_global.isSelected()) {
                globalChatAppendElement(createMessage(MessageStyle.GLOBAL_CHAT,
                        createHTMLElement(senderName, MessageStyle.BOLD),
                        createHTMLElement(message, null)
                        ));
            } else {
                globalChatAppendElement(createMessage(MessageStyle.GAME_CHAT,
                        createHTMLElement(senderName, MessageStyle.BOLD),
                        createHTMLElement(message, null)
                ));
            }

            store.dispatch(ChatReducer.SEND_MESSAGE, new ChatSendAction(receiverIds, chatMessage));
        }
        chatview_textarea_chatinput.clear();
    }

    /**
     * Sends the message from the textarea to the selected clients.
     */
    private void printAndSendWhisper() {
        String message = chatview_textarea_chatinput.getText().trim();
        String chatMessage;
        if (!message.isEmpty()) {
            List<Integer> receiverIds = new ArrayList<>();

            // don't send message to ourselves
            var receivers = tableview_chat.getSelectionModel().getSelectedItems()
                    .filtered(clientData -> clientData.clientId() != store.getState().client().getClientId());

            // no receivers selected
            if (receivers.isEmpty()) {
                return;
            }

            // creates message for the chat textarea
            chatMessage = resourceBundle.getString("youWhisper") + " ";
            for (int i = 0; i < receivers.size() - 1; i++) {
                if (receivers.get(i).clientId() == store.getState().client().getClientId()) {
                    continue;
                }
                receiverIds.add(receivers.get(i).clientId());
                chatMessage += "@" + receivers.get(i).name() + ", ";
            }
            receiverIds.add(receivers.get(receivers.size()-1).clientId());
            chatMessage += "@" + receivers.get(receivers.size()-1).name() + ": ";

            globalChatAppendElement(createMessage(
                    MessageStyle.WHISPER,
                    createHTMLElement(chatMessage, MessageStyle.BOLD),
                    createHTMLElement(message, null)
                    ));

            store.dispatch(ChatReducer.SEND_MESSAGE, new ChatSendAction(receiverIds, message));
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
    private Document getChatDocument() {
        return getDocument(chatWebView);
    }

    /**
     * Returns the Document used by the specified WebView.
     *
     * @param webView the WebView whose document to get.
     * @return The Document used by the specified WebView.
     */
    private Document getDocument(WebView webView) {

        WebEngine webEngine = webView.getEngine();
        Document doc = webEngine.getDocument();

        if (doc == null) {
            throw new NullPointerException("Document of " + webView + " was null!");
        }
        // TODO: maybe return as HTMLDocument instead
        return doc;
    }

    /**
     * Creates a html-element with a main message style and other elements.
     *
     * @param mainMessageStyle the style for the complete message.
     * @param elements one or more elements that will be inside the message.
     * @return the complete message.
     */
    private Element createMessage(MessageStyle mainMessageStyle, Node... elements) {
        Element main = createHTMLElement(mainMessageStyle);

        for (Node e : elements) {
            main.appendChild(e);
        }

        return main;
    }

    private Node createHTMLElement(String textContent, MessageStyle messageStyle) {
        if (messageStyle == null) {
            // TODO: this uses the chat doc specifically, even though it creates an HTML element usable in any doc
            return getChatDocument().createTextNode(textContent);
        } else {
            Element element = createHTMLElement(messageStyle);
            element.setTextContent(textContent);
            return element;
        }
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
        // TODO: this uses the chat doc specifically, even though it creates an HTML element usable in any doc
        return getChatDocument().createElement(tagName);
    }

    /**
     * Appends the element with the specified tag name to the global chat HTML body.
     * @param element the HTML element to be appended to the chat log.
     */
    private void globalChatAppendElement(Node element) {
        appendElement(element, chatWebView, globalChatBody);
    }

    /**
     * Appends the element with the specified tag name to the global chat HTML body.
     * @param element the HTML element to be appended to the turn log.
     */
    private void turnLogAppendElement(Node element) {
        appendElement(element, webview_log, turnLogBody);
    }

    /**
     * Appends the element with the specified tag name to the specified HTML body.
     * @param element the HTML element to be appended to the specified body.
     * @param webView the WebView displaying the specified body.
     * @param body the body to append the element to.
     */
    private void appendElement(Node element, WebView webView, Element body) {

        if (body == null) {
            System.out.println("Body of " + webView + " was null while printing: \"" + element.toString() + "\"");
            return;
        }

        WebEngine webEngine = webView.getEngine();
        // TODO: this uses the chat doc specifically, even though it creates an HTML element usable in any doc
        Document doc = getChatDocument();

        int scrollY = (Integer) webEngine.executeScript("window.scrollY");
        int scrollHeight = (Integer) webEngine.executeScript( "document.documentElement.scrollHeight");
        int clientHeight = (Integer) webEngine.executeScript( "document.body.clientHeight");
        boolean scrollToBottom = scrollY == (scrollHeight - clientHeight);

        body.appendChild(element);
        element.appendChild(doc.createElement("br"));

        if (scrollToBottom) {
            webEngine.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
        }
    }

    //region Button-functions

    /**
     * Sets the functionality for the clear selection button.
     */
    @FXML
    private void onClearSelectionButtonPressed() {
        tableview_chat.getSelectionModel().clearSelection();
    }

    /**
     * Sets the functionality for the send button.
     */
    @FXML
    private void onSendButtonPressed() {
        printAndSendMessage();
    }

    /**
     * Sets the functionality for the whisper button.
     */
    @FXML
    private void onWhisperButtonPressed() {
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

    //endregion Button-functions
}
