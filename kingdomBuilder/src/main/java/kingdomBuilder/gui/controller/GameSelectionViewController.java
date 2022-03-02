package kingdomBuilder.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kingdomBuilder.KBState;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.reducers.ApplicationReducer;
import kingdomBuilder.reducers.GameReducer;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameSelectionView.
 */
public class GameSelectionViewController extends Controller implements Initializable {

    /**
     * Layout that contains the games table (left side) and the preview of a single game (right side)
     */
    @FXML
    private HBox hbox_gameselection;

    /**
     * Layout that contains the games table with its buttons
     */
    @FXML
    private VBox vbox_table;

    /**
     * Represents the TableView that displays the games on the server.
     */
    @FXML
    private TableView<GameData> gameselection_tableview;

    @FXML
    private TableColumn<GameData, String> gameselection_column_id;

    @FXML
    private TableColumn<GameData, String> gameselection_column_name;

    @FXML
    private TableColumn<GameData, String> gameselection_column_players;

    @FXML
    private TableColumn<GameData, String> gameselection_column_status;

    @FXML
    private Label gameselection_label_gamename;

    @FXML
    private Label gameselection_label_hostname;

    @FXML
    private Label gameselection_label_timelimit;

    @FXML
    private Label gameselection_label_turnlimit;

    @FXML
    private TextArea gameselection_textarea_description;


    /**
     * Layout that contains the preview of a game
     */
    @FXML
    private VBox vbox_preview;

    /**
     * Constructs the GameSelectionViewController.
     *
     * @param store the Application's store to set the field.
     */
    public GameSelectionViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     *
     * @param location the location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupLayout();
        setupGameList();

        // updates the TableView
        store.subscribe(kbState -> gameselection_tableview.getItems().setAll(kbState.games().values()), "games");
        updateGameInformation();
    }

    /**
     * Initializes the TableView for all games.
     */
    private void setupGameList() {
        gameselection_tableview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gameselection_column_id.setCellValueFactory(param -> new SimpleStringProperty(
                String.valueOf(param.getValue().gameId())));
        gameselection_column_name.setCellValueFactory(param -> new SimpleStringProperty(
                String.valueOf(param.getValue().gameName())));
        gameselection_column_players.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().playersJoined() + "/"
                        + param.getValue().playerLimit()));
        gameselection_column_status.setCellValueFactory(param -> {
            int playerLeft = param.getValue().playerLimit() - param.getValue().playersJoined();
            return new SimpleStringProperty(
                    playerLeft > 0 ? "OPEN" : "CLOSED");
        });
    }

    private void updateGameInformation() {
        gameselection_tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {

                if (newValue == null)
                    return;
                gameselection_label_gamename.setText(newValue.gameName());
                ClientData host = store.getState().clients().get(newValue.clientId());
                gameselection_label_hostname.setText(host != null ? host.name() : "???");
                // TODO?
                //gameselection_label_timelimit;
                //gameselection_label_turnlimit;
                gameselection_textarea_description.setText(newValue.gameDescription());
            }
        });
    }

    /**
     * Initializes layout arrangement.
     */
    private void setupLayout() {
        vbox_table.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
        vbox_preview.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
    }

    /**
     * Sets the functionality for the CreateNewGame Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onCreateNewGameButtonPressed(Event event) {
        sceneLoader.showGameSettingsView(true);
    }

    /**
     * Sets the functionality for the LevelEditor Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onLevelEditorButtonPressed(Event event) {
        sceneLoader.showLevelEditorView();
    }

    /**
     * Sets the functionality for the ReturnToMenu Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onReturnToMenuButtonPressed(Event event) {
        store.dispatch(ApplicationReducer.DISCONNECT, Boolean.FALSE);
        sceneLoader.showMenuView();
    }

    /**
     * Sets the functionality for the CreateNewGame Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onJoinGamePressed(Event event) {
        int id = gameselection_tableview.getSelectionModel().getSelectedItem().gameId();
        store.dispatch(GameReducer.JOIN_GAME, id);
        sceneLoader.showGameView(false, true);
    }

    /**
     * Sets the functionality for the Refresh Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onRefreshButtonPressed(Event event) {
        //TODO: maybe use an Action instead
        store.getState().client().gamesRequest();
    }
}