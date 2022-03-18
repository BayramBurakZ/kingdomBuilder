package kingdomBuilder.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameSelectionView.
 */
public class GameSelectionViewController extends Controller implements Initializable {

    /**
     * Layout that contains the games table (left side) and the preview of a single game (right side)
     */
    @FXML
    private HBox gameselection_hbox;

    /**
     * Layout that contains the games table with its buttons
     */
    @FXML
    private VBox vbox_table;

    /**
     * Represents the ComboCox for the filter.
     */
    @FXML
    public ComboBox<String> gameselection_comboBox_filter;

    /**
     * Represents the TableView that displays the games on the server.
     */
    @FXML
    private TableView<GameData> gameselection_tableview;

    /**
     * Represents the column for the id.
     */
    @FXML
    private TableColumn<GameData, String> gameselection_column_id;

    /**
     * Represents the column for the name.
     */
    @FXML
    private TableColumn<GameData, String> gameselection_column_name;

    /**
     * Represents the column for the players.
     */
    @FXML
    private TableColumn<GameData, String> gameselection_column_players;

    /**
     * Represents the column for the status.
     */
    @FXML
    private TableColumn<GameData, String> gameselection_column_status;

    /**
     * Represents the label to display the name of the game.
     */
    @FXML
    private Label gameselection_label_gamename;

    /**
     * Represents the label to display the hostname.
     */
    @FXML
    private Label gameselection_label_hostname;

    /**
     * Represents the textarea to display the description.
     */
    @FXML
    private TextArea gameselection_textarea_description;

    /**
     * Represents the label to display the players in a game.
     */
    @FXML
    public Label gameselection_label_playersOfGame;

    /**
     * Layout that contains the preview of a game
     */
    @FXML
    private VBox vbox_preview;

    /**
     * Represents the list with the Data for the table.
     */
    private final ObservableList<GameData> masterData = FXCollections.observableArrayList();

    /**
     * Represents the list to filter the data for the table.
     */
    private final FilteredList<GameData> filteredData = new FilteredList<>(masterData, gameData -> true);

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
     * @param location  the location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupLayout();
        setupGameList();

        // updates the TableView
        store.subscribe(kbState -> {
            masterData.clear();
            masterData.addAll(kbState.games().values());
        }, "games");

        // initialize the Combobox for filtering
        gameselection_comboBox_filter.getItems().addAll("OPEN", "CLOSED", "ALL");
        gameselection_tableview.setItems(masterData);

        updateGameInformation();
    }

    /**
     * Updates the Tableview whenever a new Filter is selected.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void filterTable(Event event) {
        String filter = gameselection_comboBox_filter.getSelectionModel().getSelectedItem();
        System.out.println(filter);

        filteredData.setPredicate(gameData -> {
            if (filter.equals("OPEN") && gameData.playersJoined() < gameData.playerLimit()) return true;
            else if (filter.equals("CLOSED") && gameData.playersJoined() == gameData.playerLimit()) return true;
            else if (filter.equals("ALL")) return true;

            return false;
        });

        gameselection_tableview.setItems(filteredData);
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

    /**
     * Updates the information about a game that is currently selected.
     */
    private void updateGameInformation() {
        gameselection_tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {

                if (newValue == null)
                    return;
                gameselection_label_gamename.setText(newValue.gameName());
                ClientData host = store.getState().clients().get(newValue.clientId());
                gameselection_label_hostname.setText(host != null ? host.name() : "???");
                gameselection_textarea_description.setText(newValue.gameDescription());

                KBState kbState = store.getState();
                List<Integer> list = kbState.playersOfGame().get(newValue.gameId());
                String s = "";

                for (int i = 0; i < newValue.playersJoined(); i++) {
                    String name;
                    if (kbState.clients().containsKey(list.get(i)))
                        name = kbState.clients().get(list.get(i)).name();
                    else
                        name = "???";

                    s += (i+1) + ": " + name + "\n";
                }

                if (s.equals("")) {
                    s = "---";
                }

                gameselection_label_playersOfGame.setText(s);

            }
        });
    }

    /**
     * Initializes layout arrangement.
     */
    private void setupLayout() {
        vbox_table.prefWidthProperty().bind(gameselection_hbox.widthProperty().multiply(0.5));
        vbox_preview.prefWidthProperty().bind(gameselection_hbox.widthProperty().multiply(0.5));
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
        if (gameselection_tableview.getSelectionModel().getSelectedItem() == null) {
            // TODO: error message
            return;
        }
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