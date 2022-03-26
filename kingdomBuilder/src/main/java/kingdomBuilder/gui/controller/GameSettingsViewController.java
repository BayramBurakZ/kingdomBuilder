package kingdomBuilder.gui.controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.HostGameAction;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
    private static final int MAX_TIME_LIMIT = 20;

    /**
     * Represents the default time limit.
     */
    public static final int DEFAULT_TIME_LIMIT = 5;

    /**
     * Represents the minimal time limit the user can set.
     */
    private static final int MIN_TIME_LIMIT = 1;

    /**
     * Represents the maximal turn limit the user can set.
     */
    private static final int MAX_TURN_LIMIT = 500;

    /**
     * Represents the default turn limit.
     */
    public static final int DEFAULT_TURN_LIMIT = 50;

    /**
     * Represents the minimal turn limit the user can set.
     */
    private static final int MIN_TURN_LIMIT = 1;

    /**
     * Represents the resolution of the square quadrant preview image.
     */
    private static final int QUADRANT_RESOLUTION = 10 * 16;

    //region FXML-Imports

    /**
     * Represents the Button to go back into the previous screen.
     */
    @FXML
    private Button gamesettings_button_back;

    /**
     * Represents the ChoiceBox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_upperleft;

    /**
     * Represents the ChoiceBox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_upperright;

    /**
     * Represents the ChoiceBox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_bottomleft;

    /**
     * Represents the ChoiceBox for one Quadrant.
     */
    @FXML
    private ChoiceBox<Integer> gamesettings_choicebox_bottomright;

    /**
     * Represents GridPane for the ChoiceBoxes for the quadrants.
     */
    @FXML
    private GridPane gamesettings_gridpane_ids;

    /**
     * Represents GridPane for the previews of the quadrants.
     */
    @FXML
    public GridPane gamesettings_gridpane_previews;

    /**
     * Represents the spinner, where the user sets, how many (human-)players can join.
     */
    @FXML
    private Spinner<Integer> gamesettings_spinner_online_players;

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
     * Represents the TextField, to set the name of the game.
     */
    @FXML
    private TextField gamesettings_textfield_name;

    /**
     * Represents the TextField, to set the description of the game.
     */
    @FXML
    private TextField gamesettings_textfield_desc;

    /**
     * Represents the ImageView showing the currently selected upper left quadrant.
     */
    @FXML
    public ImageView gamsettings_quadrant_upperleft;

    /**
     * Represents the ImageView showing the currently selected lower left quadrant.
     */
    @FXML
    public ImageView gamsettings_quadrant_lowerleft;

    /**
     * Represents the ImageView showing the currently selected upper right quadrant.
     */
    @FXML
    public ImageView gamsettings_quadrant_upperright;

    /**
     * Represents the ImageView showing the currently selected lower right quadrant.
     */
    @FXML
    public ImageView gamsettings_quadrant_lowerright;

    /**
     * Represents the VBox containing all the legend elements that describe the quadrant previews.
     */
    @FXML
    public VBox gamesettings_legend_vbox;

    //endregion FXML-Imports

    /**
     * Represents the image of the upper left quadrant preview.
     */
    private WritableImage quadrantUpperLeft;

    /**
     * Represents the image of the lower left quadrant preview.
     */
    private WritableImage quadrantLowerLeft;

    /**
     * Represents the image of the upper right quadrant preview.
     */
    private WritableImage quadrantUpperRight;

    /**
     * Represents the image of the lower right quadrant preview.
     */
    private WritableImage quadrantLowerRight;

    /**
     * Represents if the menu is for a local (0) or online (1) game.
     */
    private boolean isOnlineGame;

    /**
     * Represents the value of human players, that can join the game.
     */
    private int playerCount = 0;

    /**
     * Represents the map specifying what color TileTypes should have for the quadrant previews.
     */
    private final Map<TileType, Color> tileTypeColorMap = new HashMap<>();

    /**
     * Constructs the GameSettingsViewController.
     *
     * @param store the Application's store to set the field.
     */
    public GameSettingsViewController(Store<KBState> store) {
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
        // set Text depending if the game is local or online
        if (isOnlineGame) {
            gamesettings_button_back.setText(resources.getString("backToGameList"));
        } else {
            gamesettings_button_back.setText(resources.getString("backToMenu"));
        }

        setupLayout();
        initializeChoiceBox();
        initializeSpinner();
        initializeListeners();
        initializeQuadrantPreviews();
        initializeLegend();
    }

    /**
     * Initializes all quadrant previews.
     */
    private void initializeQuadrantPreviews() {
        // JavaFX doesn't support nearest neighbour interpolation, so you gotta deal with upressing the image itself
        quadrantUpperLeft = new WritableImage(QUADRANT_RESOLUTION, QUADRANT_RESOLUTION);
        quadrantLowerLeft = new WritableImage(QUADRANT_RESOLUTION, QUADRANT_RESOLUTION);
        quadrantUpperRight = new WritableImage(QUADRANT_RESOLUTION, QUADRANT_RESOLUTION);
        quadrantLowerRight = new WritableImage(QUADRANT_RESOLUTION, QUADRANT_RESOLUTION);

        gamsettings_quadrant_upperleft.setImage(quadrantUpperLeft);
        gamsettings_quadrant_lowerleft.setImage(quadrantLowerLeft);
        gamsettings_quadrant_upperright.setImage(quadrantUpperRight);
        gamsettings_quadrant_lowerright.setImage(quadrantLowerRight);

        gamsettings_quadrant_upperleft.setFitWidth(QUADRANT_RESOLUTION);
        gamsettings_quadrant_lowerleft.setFitWidth(QUADRANT_RESOLUTION);
        gamsettings_quadrant_upperright.setFitWidth(QUADRANT_RESOLUTION);
        gamsettings_quadrant_lowerright.setFitWidth(QUADRANT_RESOLUTION);
        gamsettings_quadrant_upperleft.setFitHeight(QUADRANT_RESOLUTION);
        gamsettings_quadrant_lowerleft.setFitHeight(QUADRANT_RESOLUTION);
        gamsettings_quadrant_upperright.setFitHeight(QUADRANT_RESOLUTION);
        gamsettings_quadrant_lowerright.setFitHeight(QUADRANT_RESOLUTION);

        // assigning colors to TileTypes
        tileTypeColorMap.put(TileType.GRAS, Color.LAWNGREEN);
        tileTypeColorMap.put(TileType.FLOWER, Color.PALEGREEN);
        tileTypeColorMap.put(TileType.FORREST, Color.FORESTGREEN);
        tileTypeColorMap.put(TileType.CANYON, Color.SADDLEBROWN);
        tileTypeColorMap.put(TileType.DESERT, Color.LIGHTYELLOW);
        tileTypeColorMap.put(TileType.WATER, Color.BLUE);
        tileTypeColorMap.put(TileType.MOUNTAIN, Color.DARKGRAY);
        tileTypeColorMap.put(TileType.CASTLE, Color.LIGHTGRAY);
        tileTypeColorMap.put(TileType.ORACLE, Color.MEDIUMPURPLE);
        tileTypeColorMap.put(TileType.FARM, Color.YELLOW);
        tileTypeColorMap.put(TileType.TAVERN, Color.BURLYWOOD);
        tileTypeColorMap.put(TileType.TOWER, Color.DARKSLATEGRAY);
        tileTypeColorMap.put(TileType.HARBOR, Color.CADETBLUE);
        tileTypeColorMap.put(TileType.PADDOCK, Color.BROWN);
        tileTypeColorMap.put(TileType.BARN, Color.ROSYBROWN);
        tileTypeColorMap.put(TileType.OASIS, Color.TURQUOISE);

        updateQuadrant(quadrantUpperLeft, gamesettings_choicebox_upperleft.getValue());
        updateQuadrant(quadrantLowerLeft, gamesettings_choicebox_bottomleft.getValue());
        updateQuadrant(quadrantUpperRight, gamesettings_choicebox_upperright.getValue());
        updateQuadrant(quadrantLowerRight, gamesettings_choicebox_bottomright.getValue());
    }

    /**
     * Initializes the legend of the quadrant previews.
     */
    private void initializeLegend() {
        var children = gamesettings_legend_vbox.getChildren();
        for (var entry : tileTypeColorMap.entrySet()) {
            Rectangle rectangle = new Rectangle(20, 20, entry.getValue());
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1);
            Label label = new Label(entry.getKey().toStringLocalized());
            HBox hbox = new HBox(rectangle, label);
            hbox.setSpacing(5);
            children.add(hbox);
        }
    }

    /**
     * Initializes all Listeners.
     */
    private void initializeListeners() {
        setupSpinnerListener();
        setupCheckboxListener();

        gamesettings_textfield_name.textProperty().addListener(observable -> updateButtons());
        gamesettings_textfield_desc.textProperty().addListener(observable -> updateButtons());

        gamesettings_choicebox_upperleft.valueProperty().addListener(
                observable -> updateQuadrant(quadrantUpperLeft, gamesettings_choicebox_upperleft.getValue()));
        gamesettings_choicebox_upperright.valueProperty().addListener(
                observable -> updateQuadrant(quadrantUpperRight, gamesettings_choicebox_upperright.getValue()));
        gamesettings_choicebox_bottomleft.valueProperty().addListener(
                observable -> updateQuadrant(quadrantLowerLeft, gamesettings_choicebox_bottomleft.getValue()));
        gamesettings_choicebox_bottomright.valueProperty().addListener(
                observable -> updateQuadrant(quadrantLowerRight, gamesettings_choicebox_bottomright.getValue()));
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
     * Initializes the ChoiceBoxes for the quadrant id selection.
     */
    private void initializeChoiceBox() {
        gamesettings_choicebox_upperleft.setItems(FXCollections.observableList(store.getState().quadrants().keySet().stream().toList()));
        gamesettings_choicebox_upperright.setItems(FXCollections.observableList(store.getState().quadrants().keySet().stream().toList()));
        gamesettings_choicebox_bottomleft.setItems(FXCollections.observableList(store.getState().quadrants().keySet().stream().toList()));
        gamesettings_choicebox_bottomright.setItems(FXCollections.observableList(store.getState().quadrants().keySet().stream().toList()));
        gamesettings_choicebox_upperleft.getSelectionModel().select(0);
        gamesettings_choicebox_upperright.getSelectionModel().select(1);
        gamesettings_choicebox_bottomleft.getSelectionModel().select(2);
        gamesettings_choicebox_bottomright.getSelectionModel().select(3);
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
     * Initializes the Spinner values for time and turn.
     */
    private void initializeSpinner() {
        // time limit
        gamesettings_spinner_time.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_TIME_LIMIT, MAX_TIME_LIMIT, DEFAULT_TIME_LIMIT)
        );

        // turn limit
        gamesettings_spinner_turn.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_TURN_LIMIT, MAX_TURN_LIMIT, DEFAULT_TURN_LIMIT)
        );

        // 0 - 4 players
        gamesettings_spinner_online_players.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_PLAYERS, playerCount)
        );
    }

    /**
     * Sets the Spinner listeners to adjust to the player limit.
     */
    private void setupSpinnerListener() {
        gamesettings_spinner_online_players.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerCount = newValue;

            // update Buttons
            updateButtons();
        });
    }

    /**
     * Updates the Buttons according to the state of the spinners.
     */
    private void updateButtons() {
        // not all information to host a game are filled in
        if (hasAllHostInformation()) {
            return;
        }
        // no players
        if (playerCount == 0) {
            deactivateHostGameButton();
        } else {
            activateHostGameButton();
        }
    }

    /**
     * Updates the specified quadrant preview according to the state of the ChoiceBoxes.
     */
    private void updateQuadrant(WritableImage quadrant, Integer quadrantId) {
        PixelWriter pw = quadrant.getPixelWriter();
        TileType[] tileTypes = store.getState().quadrants().get(quadrantId);
        if (tileTypes != null) {
            int i = 0;
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                    Color color = tileTypeColorMap.get(tileTypes[i++]);
                    //pw.setColor(x, y, color);
                    ///*
                    int pixelSize = QUADRANT_RESOLUTION / 10;
                    for (int w = 0; w < pixelSize; w++) {
                        for (int h = 0; h < pixelSize; h++) {
                            pw.setColor(pixelSize * x + w, pixelSize * y + h, color);
                        }
                    }
                    //*/
                }
            }
        }
        updateButtons();
    }

    /**
     * Checks if all information are given to host a game.
     *
     * @return True, if something is missing.
     */
    private boolean hasAllHostInformation() {
        if (gamesettings_textfield_name.getText().trim().isEmpty()) {
            return true;
        }

        if (gamesettings_textfield_desc.getText().trim().isEmpty()) {
            return true;
        }

        if (gamesettings_choicebox_bottomright.getValue() == null) {
            return true;
        }

        if (gamesettings_choicebox_bottomleft.getValue() == null) {
            return true;
        }

        if (gamesettings_choicebox_upperleft.getValue() == null) {
            return true;
        }

        if (gamesettings_choicebox_upperright.getValue() == null) {
            return true;
        }

        // debug
        //System.out.println("All Information are set");
        return false;
    }

    /**
     * Collects the information to host a game from GUI and if successful it creates a new game.
     *
     * @return true if something is missing to host a game.
     */
    private boolean hostGame() {
        if (hasAllHostInformation()) {
            return true;
        }
        String gameName = gamesettings_textfield_name.getText().trim();
        String gameDesc = gamesettings_textfield_desc.getText().trim();

        // gets the time limit from GUI
        int timeLimit;
        if (gamesettings_checkBox_time.isSelected()) {
            // transform seconds in milliseconds
            timeLimit = gamesettings_spinner_time.getValue() * 1000 * 60;
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

        // player limit
        if (playerCount == 0) {
            return true;
        }

        int quadrantUpperLeft = gamesettings_choicebox_upperleft.getValue();
        int quadrantUpperRight = gamesettings_choicebox_upperright.getValue();
        int quadrantBottomLeft = gamesettings_choicebox_bottomleft.getValue();
        int quadrantBottomRight = gamesettings_choicebox_bottomright.getValue();

        System.out.println(
                "Players: " + playerCount + "\n" +
                        "Name: " + gameName + "\n" +
                        "Desc: " + gameDesc + "\n" +
                        "Time: " + timeLimit + "\n" +
                        "Turn: " + turnLimit + "\n" +
                        "QUL: " + quadrantUpperLeft + "\n" +
                        "QUR: " + quadrantUpperRight + "\n" +
                        "QBL: " + quadrantBottomLeft + "\n" +
                        "QBR: " + quadrantBottomRight + "\n"
        );

        store.dispatchOld(new HostGameAction(gameName, gameDesc, playerCount, timeLimit, turnLimit,
                quadrantUpperLeft, quadrantUpperRight, quadrantBottomLeft, quadrantBottomRight));

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
     * Sets the functionality for the Host Game Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onHostGameButtonPressed(Event event) {
        if (hostGame())
            return;
        sceneLoader.showGameSelectionView();
    }

    /**
     * Sets the functionality for the Host and Join Game Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onHostAndJoinGameButtonPressed(Event event) {
        if (hostGame())
            return;
        // TODO: Network Message "join" (with id from host-answer)
        sceneLoader.showGameView(false, isOnlineGame);
    }

    /**
     * Sets the functionality for the Host and Spectate Game Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    void onHostAndSpectateGameButtonPressed(Event event) {
        if (hostGame())
            return;
        // TODO: Network Message "spectate" (with id from host-answer)
        sceneLoader.showGameView(true, isOnlineGame);
    }

    /**
     * Sets the functionality for the GameList Button.
     *
     * @param event contains the data from the event source.
     */
    @FXML
    private void onBackButtonPressed(Event event) {
        if (isOnlineGame) {
            sceneLoader.showGameSelectionView();
        } else {
            sceneLoader.showMenuView();
        }
    }

    /**
     * Sets the field if the game is local or online.
     *
     * @param isOnlineGame true for online, false for local.
     */
    public void setIsOnlineGame(boolean isOnlineGame) {
        this.isOnlineGame = isOnlineGame;
    }
}
