package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameLobbyView.
 */
public class GameSettingsViewController extends Controller implements Initializable {
    /**
     * Represents the player limit.
     */
    private static final int MAX_PLAYERS = 4;

    /**
     * Represents the maximal time limit the user can set.
     */
    private static final int MAX_TIME_LIMIT = Integer.MAX_VALUE;

    /**
     * Represents the minimal time limit the user can set.
     */
    private static final int MIN_TIME_LIMIT = 10;

    /**
     * Represents the maximal turn limit the user can set.
     */
    private static final int MAX_TURN_LIMIT = Integer.MAX_VALUE;

    /**
     * Represents the minimal turn limit the user can set.
     */
    private static final int MIN_TURN_LIMIT = 1;

    //region FXML-Imports

    /**
     * Represents the Button to go back into the previous screen.
     */
    @FXML
    private Button gamesettings_button_back;

    /**
     * Represents the Choicebox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_upperleft;

    /**
     * Represents the Choicebox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_upperright;

    /**
     * Represents the Choicebox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_bottomleft;

    /**
     * Represents the Choicebox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_bottomright;

    /**
     * Represents Gridpane for the Choiceboxes for the quadrants.
     */
    @FXML
    private GridPane gamesettings_gridpane_ids;

    /**
     * Represents the spinner, where the user sets, how many (humen-)players can join.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_online_players;

    /**
     * Represents the spinner, where the user sets, how many bots the game should contain.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_bots;

    /**
     * Represents the spinner, where the user sets, how many hotseat players should contain.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_hotseat;

    /**
     * Represents the checkBox if a time limit is preferred.
     */
    @FXML
    private CheckBox gamesettings_checkBox_time;

    /**
     * Represents the spinner, where the user sets the time limit.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_time;

    /**
     * Represents the checkBox if a turn limit is preferred.
     */
    @FXML
    private CheckBox gamesettings_checkBox_turn;

    /**
     * Represents the spinner, where the user sets the turn limit.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_turn;

    /**
     * Represents the button to host a game.
     */
    @FXML
    private Button gamesettings_button_host;

    /**
     * Represents the button to host a game and join it.
     */
    @FXML
    private Button gamesettings_button_host_join;

    /**
     * Represents the button to host a game and spectate it.
     */
    @FXML
    private Button gamesettings_button_host_spectate;

    /**
     * Represents the textfield, to set the name of the game.
     */
    @FXML
    private TextField gamesettings_textfield_name;

    /**
     * Represents the textfield, to set the describtion of the game.
     */
    @FXML
    private TextField gamesettings_textfield_desc;

    //endregion FXML-Imports

    /**
     * Represents if the menu is for a local (0) or online (1) game.
     */
    private boolean isOnlineGame;

    /**
     * Represents the value of human players, that can join the game.
     */
    private int playerCount = 0;
    /**
     * Represents the value of bot players, that joins the game.
     */
    private int botCount = 0;
    /**
     * Represents the value of hotseat players, that joins a game.
     */
    private int hotseatCount = 0;

    /**
     * Constructs the GameSettingsViewController.
     * @param store The Application's store to set the field.
     */
    public GameSettingsViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set Text depending if the game is local or online
        if (isOnlineGame) {
            gamesettings_button_back.setText(resources.getString("backToGameList"));
        } else {
            gamesettings_button_back.setText(resources.getString("backToMenu"));
        }

        setupLayout();
        initializeChoiceBox();

        setupCheckboxListener();

        initializeSpinner();
        setupSpinnerListener();
    }

    /**
     * Sets the checkbox listeners for time limit and turn limit.
     */
    private void setupCheckboxListener() {
        gamesettings_checkBox_time.selectedProperty().addListener((observable, oldValue, newValue) ->
                gamesettings_spinner_time.setDisable(!newValue));

        gamesettings_checkBox_turn.selectedProperty().addListener((observable, oldValue, newValue) ->
                gamesettings_spinner_turn.setDisable(!newValue));
    }

    /**
     * Initialize the choice boxes for the quadrant id selection.
     */
    private void initializeChoiceBox() {
        gamesettings_choicebox_upperleft.setItems(store.getState().quadrantIDs);
        gamesettings_choicebox_upperright.setItems(store.getState().quadrantIDs);
        gamesettings_choicebox_bottomleft.setItems(store.getState().quadrantIDs);
        gamesettings_choicebox_bottomright.setItems(store.getState().quadrantIDs);
    }

    /**
     * Initializes layout arrangement.
     */
    private void setupLayout() {
        gamesettings_choicebox_upperleft.prefWidthProperty().bind(
                gamesettings_gridpane_ids.widthProperty().multiply(0.5));
        gamesettings_choicebox_upperright.prefWidthProperty().bind(
                gamesettings_gridpane_ids.widthProperty().multiply(0.5));
        gamesettings_choicebox_bottomleft.prefWidthProperty().bind(
                gamesettings_gridpane_ids.widthProperty().multiply(0.5));
        gamesettings_choicebox_bottomright.prefWidthProperty().bind(
                gamesettings_gridpane_ids.widthProperty().multiply(0.5));
    }

    /**
     * Initialize the spinner values for time and turn.
     */
    private void initializeSpinner() {
        // playerlimit
        gamesettings_spinner_time.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_TIME_LIMIT, MAX_TIME_LIMIT, 1800)
        );

        // timelimit
        gamesettings_spinner_turn.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_TURN_LIMIT, MAX_TURN_LIMIT, 20)
        );

        // 0 - 4 players
        gamesettings_spinner_online_players.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_PLAYERS, playerCount)
        );

        gamesettings_spinner_bots.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_PLAYERS, botCount)
        );

        gamesettings_spinner_hotseat.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_PLAYERS - 1, hotseatCount)
        );
    }

    /**
     * Sets the spinner listeners to adjust to the player limit.
     */
    private void setupSpinnerListener() {
        gamesettings_spinner_online_players.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerCount = newValue;

            // Impossible to create game without players but with hotseat player
            if (playerCount == 0) {
                deactivateHostAndJoinGameButton();
            } else {
                activateHostAndJoinGameButton();
            }

            // update Buttons
            updateButtons();
            // update values in bot spinner
            updateBotSpinner();
            // update values in hotseat spinner
            updateHotseatSpinner();
        });

        gamesettings_spinner_hotseat.valueProperty().addListener((observable, oldValue, newValue) -> {
            hotseatCount = newValue;
            // need to join game with min 1 hotseat-player
            if (hotseatCount > 0) {
                deactivateHostGameButton();
            } else {
                activateHostGameButton();
            }

            // update Buttons
            updateButtons();
            // update values in bot spinner
            updateBotSpinner();
            // update values in player spinner
            updatePlayerSpinner();
        });

        gamesettings_spinner_bots.valueProperty().addListener((observable, oldValue, newValue) -> {
            botCount = newValue;

            // update Buttons
            updateButtons();
            // update values in hotseat spinner
            updateHotseatSpinner();
            // update values in player spinner
            updatePlayerSpinner();
        });
    }

    /**
     * Updates the spinner for bots.
     */
    private void updateBotSpinner() {
        int currentValue = gamesettings_spinner_bots.getValue();
        int max_bots = MAX_PLAYERS - playerCount - hotseatCount;

        if (currentValue > max_bots) {
            currentValue = max_bots;
            botCount = currentValue;
        }

        gamesettings_spinner_bots.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max_bots, currentValue));
    }

    /**
     * Updates the spinner for hotseat players.
     */
    private void updateHotseatSpinner() {
        int currentValue = gamesettings_spinner_hotseat.getValue();
        int max_hotseat = MAX_PLAYERS - playerCount - botCount;

        if (currentValue > max_hotseat) {
            currentValue = max_hotseat;
            hotseatCount = currentValue;
        }

        gamesettings_spinner_hotseat.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max_hotseat, currentValue));
    }

    /**
     * Updates the spinner for human players.
     */
    private void updatePlayerSpinner() {
        int currentValue = gamesettings_spinner_online_players.getValue();
        int max_players = MAX_PLAYERS - botCount - hotseatCount;

        if (currentValue > max_players) {
            currentValue = max_players;
            playerCount = currentValue;
        }

        // no hotseat allowed without own player
        if (currentValue == 0) {
            gamesettings_spinner_hotseat.getValueFactory().setValue(0);
            hotseatCount = 0;
        }

        gamesettings_spinner_online_players.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max_players, currentValue));
    }

    /**
     * Updates the Buttons according to the state of the spinners.
     */
    private void updateButtons() {
        // no players selected or only hotseat
        if (playerCount + hotseatCount + botCount == 0 || (hotseatCount > 0 && playerCount == 0)) {
            deactivateHostGameButton();
            deactivateHostAndJoinGameButton();
            deactivateHostAndSpectateGameButton();
        }

        // min 1 playable player and:
        // no online player and min 1 hotseat / no hotseat and min 1 bot
        // -> impossible to host and not join/spectate
        if (playerCount > 0 && ((hotseatCount >= 0 && botCount == 0) || (hotseatCount == 0 && botCount >= 0))) {
            deactivateHostGameButton();
            activateHostAndJoinGameButton();
            activateHostAndSpectateGameButton();
        }

        // with only players you can host, host and join or spectate
        if (playerCount > 0 && hotseatCount == 0 && botCount == 0) {
            activateHostGameButton();
            activateHostAndJoinGameButton();
            activateHostAndSpectateGameButton();
        }

        // with only bots you can spectate a game, but neither host nor join
        if  (playerCount == 0 && hotseatCount == 0 && botCount > 0) {
            deactivateHostGameButton();
            deactivateHostAndJoinGameButton();
            activateHostAndSpectateGameButton();
        }
    }

    /**
     * Sets the functionality for the Host Game Button.
     */
    @FXML
    private void onButtonHostGamePressed() {
        if(hostGame())
            return;
        sceneLoader.showGameSelectionView();
    }

    /**
     * Sets the functionality for the Host and Join Game Button.
     */
    @FXML
    private void onButtonHostAndJoinGamePressed() {
        if(hostGame())
            return;
        // TODO: Network Message "join" (with id from host-answer)
        sceneLoader.showGameView(false, isOnlineGame);
    }

    /**
     * Sets the functionality for the Host and Spectate Game Button.
     */
    @FXML void onButtonHostAndSpectateGamePressed() {
        if(hostGame())
            return;
        // TODO: Network Message "spectate" (with id from host-answer)
        sceneLoader.showGameView(true, isOnlineGame);
    }

    /**
     * Collects the information to host a game from GUI and if successful it creates a new game.
     * @return true if something is missing to host a game.
     */
    private boolean hostGame() {
        String gameName = gamesettings_textfield_name.getText().trim();
        String gameDesc = gamesettings_textfield_desc.getText().trim();

        if(gameName.isEmpty() || gameDesc.isEmpty()) {
            return true;
        }

        // gets the time limit from GUI
        int timeLimit;
        if (gamesettings_checkBox_time.isSelected()) {
            // transform seconds in milliseconds
            timeLimit = gamesettings_spinner_time.getValue() * 1000;
        } else {
            timeLimit = -1;
        }

        // get the turn limit from GUI
        int turnLimit;
        if (gamesettings_checkBox_turn.isSelected()) {
            turnLimit = gamesettings_spinner_turn.getValue();
        } else {
            turnLimit = -1;
        }

        int playerLimit = botCount + hotseatCount + playerCount;
        if (playerLimit == 0) {
            return true;
        }

        // if no quadrant is selected
        if (gamesettings_choicebox_upperleft.getValue() == null)
            return true;
        if (gamesettings_choicebox_upperright.getValue() == null)
            return true;
        if (gamesettings_choicebox_bottomleft.getValue() == null)
            return true;
        if (gamesettings_choicebox_bottomright.getValue() == null)
            return true;

        int quadrantUpperLeft = gamesettings_choicebox_upperleft.getValue();
        int quadrantUpperRight = gamesettings_choicebox_upperright.getValue();
        int quadrantBottomLeft = gamesettings_choicebox_bottomleft.getValue();
        int quadrantBottomRight = gamesettings_choicebox_bottomright.getValue();

        System.out.println(
                "Name: " + gameName + "\n" +
                "Desc: " + gameDesc + "\n" +
                "Time: " + timeLimit + "\n" +
                "Turn: " + turnLimit + "\n" +
                "QUL: " + quadrantUpperLeft + "\n" +
                "QUR: " + quadrantUpperRight + "\n" +
                "QBL: " + quadrantBottomLeft + "\n" +
                "QBR: " + quadrantBottomRight + "\n"
        );
        //TODO: Network Message "host"
        return false;
    }

    /**
     * Activates the Host Game Button.
     */
    private void activateHostGameButton() {
        // if the game is an online game, the user can host a game
        // else the user is forced to join the game
        if (isOnlineGame) {
            gamesettings_button_host.setDisable(false);
        }
    }

    /**
     * Deactivates the Host Game Button.
     */
    private void deactivateHostGameButton() {
        gamesettings_button_host.setDisable(true);
    }

    /**
     * Activates the Host Game and Join Button.
     */
    private void activateHostAndJoinGameButton() {
        gamesettings_button_host_join.setDisable(false);
    }

    /**
     * Deactivates the Host Game and Join Button.
     */
    private void deactivateHostAndJoinGameButton() {
        gamesettings_button_host_join.setDisable(true);
    }

    /**
     * Activates the Host Game and Spectate Button.
     */
    private void activateHostAndSpectateGameButton() {
        gamesettings_button_host_spectate.setDisable(false);
    }

    /**
     * Deactivates the Host Game and Spectate Button.
     */
    private void deactivateHostAndSpectateGameButton() {
        gamesettings_button_host_spectate.setDisable(true);
    }

    /**
     * Sets the functionality for the GameList Button.
     */
    @FXML
    private void onButtonBackPressed() {
        if (isOnlineGame) {
            sceneLoader.showGameSelectionView();
        } else {
            sceneLoader.showMenuView();
        }
    }

    /**
     * Sets the field if the game is local or online.
     * @param isOnlineGame true for online, false for local.
     */
    public void setIsOnlineGame(boolean isOnlineGame) {
        this.isOnlineGame = isOnlineGame;
    }
}
