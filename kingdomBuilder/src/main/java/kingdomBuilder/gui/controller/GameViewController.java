package kingdomBuilder.gui.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.gui.Fog;
import kingdomBuilder.gui.GameCamera;
import kingdomBuilder.gui.gameboard.*;
import kingdomBuilder.gui.util.Util;
import kingdomBuilder.network.protocol.MyGameReply;
import kingdomBuilder.reducers.BotReducer;
import kingdomBuilder.reducers.GameReducer;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * This class controls all functions for the GameView.
 */
public class GameViewController extends Controller implements Initializable {

    /**
     * Represents the minimum depth the camera is allowed to zoom away from the board.
     */
    public static final int MIN_CAMERA_DEPTH = -1500;

    /**
     * Represents the maximum depth the camera is allowed to zoom towards the board.
     */
    public static final int MAX_CAMERA_DEPTH = -250;

    //region FXML-Imports

    /**
     * Represents the initial VBox.
     */
    @FXML
    private VBox game_vbox;

    /**
     * Represents the HBox that contains the statistics of the game at the top of the screen.
     */
    @FXML
    private HBox game_hbox_statistic;

    /**
     * Represents the HBox that contains the players.
     */
    @FXML
    private HBox game_hbox_players;

    /**
     * Represents the Label for the turn counter.
     */
    @FXML
    private Label game_label_turn;
    /**
     * Represents the Label for the time.
     */
    @FXML
    private Label game_label_time;
    /**
     * Represents the HBox where the winconditions are displayed.
     */
    @FXML
    private HBox game_hBox_windconditions;
    /**
     * Represents the HBox that contains the subscene
     */
    @FXML
    private HBox game_hbox_subscene;
    /**
     * Represents the SubScene to show the board.
     */
    @FXML
    private SubScene game_subscene;
    /**
     * Represents the HBox that contains every player specific information.
     */
    @FXML
    private HBox game_hbox_playerinformation;
    /**
     * Represents the VBox to display the terrain card.
     */
    @FXML
    private VBox game_vbox_terraincard;
    /**
     * Represents the Rectangle for the terrain image.
     */
    @FXML
    private Rectangle game_rectangle_card;
    /**
     * Represents the Label that describes the terrain type.
     */
    @FXML
    private Label game_label_carddescribtion;
    /**
     * Represents the Hbox that contains the tokens.
     */
    @FXML
    private HBox game_hbox_tokens;
    /**
     * Represents the Button to end the turn.
     */
    @FXML
    private Button game_button_end;

    /**
     * Represents the pane for all hexagons
     */
    @FXML
    private Group gameBoard_group;

    /**
     * Represents the Label to display the settlements left for the basic turn.
     */
    @FXML
    public Label game_label_basic;

    //endregion FXML

    /**
     * Represents the control menu to add Bots
     */
    private final VBox game_vBox_addPlayerVBox = new VBox();

    /**
     * Represents the gameBoard with data for the gui like hexagons and textures.
     */
    public GameBoard gameBoard;

    /**
     * Represents the center of the game-board as a Point3D.
     */
    private Point3D boardCenter;

    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

    /**
     * Represents if the view is in spectating mode.
     */
    private boolean isSpectating;

    /**
     * Represents if the view is in online mode.
     */
    private boolean isOnline;

    /**
     * Represents if the Tokens are currently disabled.
     */
    private boolean areTokensDisabled = false;

    /**
     * Represents all Tokens on the GUI of a player.
     */
    private final ArrayList<Token> tokens = new ArrayList<>();

    /**
     * Represents the list of players on the top of the view.
     */
    private final ArrayList<PlayerInformation> players = new ArrayList<>();

    /**
     * Represents the state whether a map is currently loaded.
     */
    boolean hasMap = false;

    /**
     * Represents the shadow that helps highlight the selectable hexagons.
     */
    private Fog shadow;

    /**
     * The time remaining in the current turn.
     */
    private int timeRemaining;

    /**
     * The maximum allowed time of one turn.
     */
    private int timeLimit;

    /**
     * The timeline used for regularly updating the displayed time limit and remaining time.
     */
    private Timeline timeLabelTimeline;


    /**
     * Constructs the GameView with the given store.
     *
     * @param store the Store for access to the state.
     */
    public GameViewController(Store<KBState> store) {
        this.store = store;
        this.gameBoard = new GameBoard(store);
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
        resourceBundle = resources;

        //region subscribe methods

        // START TURN
        store.subscribe(kbState -> {
            if (kbState.nextPlayer() >= 0 && kbState.gameStarted() && kbState.nextTerrainCard() != null) {
                // only preview for the local players on this PC.
                if (isNextLocalClient(kbState))
                    highlightTerrain(Game.allBasicTurnTiles(
                            kbState.gameMap(), kbState.playersMap().get(kbState.nextPlayer())));
            }
        }, "gameStarted", "nextPlayer", "nextTerrainCard");

        store.subscribe(this::onMyGameReplyChanged, "myGameReply");
        store.subscribe(this::onGameMapChanged, "gameMap");
        store.subscribe(this::onPlayersChanged, "players");
        store.subscribe(this::onTokenChanged, "token");
        store.subscribe(this::onWinConditionsChanged, "winConditions");
        store.subscribe(this::onTerrainCardChanged, "nextTerrainCard");
        store.subscribe(this::onLastTurnChanged, "gameLastTurn");
        store.subscribe(this::onNextPlayer, "nextPlayer");
        store.subscribe(this::onCurrentPlayerChanged, "currentPlayer");
        store.subscribe(this::onTurnCountChanged, "turnCount");

        // WIN SITUATION
        store.subscribe(state -> {
            if (store.getState().scores() != null) {
                if (isSpectating) {
                    store.dispatch(GameReducer.UNSPECTATE_GAME, null);
                    setSpectating(false);
                }
                sceneLoader.showWinView(state.scores());
                hasMap = false;
            }
        }, "scores");

        // endregion

        // set the initial layout of the view
        setupLayout();

        showPreStartButtons(true);

        store.subscribe(kbState -> {
            if (kbState.gameStarted())
                showPreStartButtons(false);
        }, "gameStarted");

        game_subscene.setFill(Color.LIGHTSKYBLUE);
    }

    /**
     * Shows the Buttons to add Hotseat or Bots to the game.
     *
     * @param isDisplayed whether the buttons should be displayed.
     */
    private void showPreStartButtons(boolean isDisplayed) {
        // initialize everything
        Button game_button_addBot = new Button("Add Bot");
        // Button game_button_addHotseat = new Button("Add Hotseat");
        ComboBox<BotDifficulty> game_comboBox_difficulty = new ComboBox<>();
        HBox game_hBox_addPlayerHBox = new HBox();

        // add the items in the comboBox
        game_comboBox_difficulty.getItems().addAll(
                BotDifficulty.EASY,
                BotDifficulty.NORMAL,
                BotDifficulty.HARD,
                BotDifficulty.EXPERT
        );

        // setup the ComboBox
        game_comboBox_difficulty.setConverter(new StringConverter<BotDifficulty>() {
            @Override
            public String toString(BotDifficulty object) {
                return object.toString();
            }

            @Override
            public BotDifficulty fromString(String string) {
                return null;
            }
        });
        game_comboBox_difficulty.getSelectionModel().selectFirst();

        // decide if it should be shown
        if (isDisplayed) {
            game_hBox_addPlayerHBox.getChildren().addAll(game_button_addBot);
            game_vBox_addPlayerVBox.getChildren().addAll(game_comboBox_difficulty, game_hBox_addPlayerHBox);
            game_hbox_players.getChildren().addAll(game_vBox_addPlayerVBox);
        } else {
            game_hbox_players.getChildren().remove(game_vBox_addPlayerVBox);
        }

        HBox.setMargin(game_vBox_addPlayerVBox, new Insets(10, 10, 10, 10));

        // bind width properties
        game_comboBox_difficulty.prefWidthProperty().bind(game_vBox_addPlayerVBox.widthProperty());

        // event handler when "Add Bot" is pressed
        game_button_addBot.setOnAction(event -> {
            BotDifficulty difficulty = game_comboBox_difficulty.getSelectionModel().getSelectedItem();
            game_hbox_players.getChildren().remove(game_vBox_addPlayerVBox);
            store.dispatch(BotReducer.CONNECT_BOT, difficulty);
        });
    }

    /**
     * Updates the GUI whenever the currentPlayer changes.
     *
     * @param kbState the current state.
     */
    private void onNextPlayer(KBState kbState) {
//        if (kbState.mainClient() != null) {
//            String msg = String.format("(%d) Called onNextPlayer", kbState.mainClient().getClientId());
//            System.out.println(msg);
//        }

        if (kbState.nextPlayer() == -1 || kbState.mainClient() == null)
            return;

        game_button_end.setDisable(!isSpectating);
        basicTurnLeft(kbState);

        // Highlight whose turn it is
        setCurrentPlayerHighlight(kbState.players(), kbState.currentPlayer());
    }

    /**
     * Updates the GUI whenever a turn is made.
     *
     * @param kbState the current state.
     */
    private void onLastTurnChanged(KBState kbState) {
        Turn turn = kbState.gameLastTurn();

        if (turn == null)
            return;

        if (kbState.gameLastTurn() instanceof ServerTurn) {

            ServerTurn lastTurn = (ServerTurn) turn;
            PlayerColor color = kbState.playersMap().get(lastTurn.clientId).color;

            switch (lastTurn.type) {
                case PLACE -> setServerSettlement(lastTurn.x, lastTurn.y, color);
                case MOVE -> moveServerSettlement(lastTurn.x, lastTurn.y, lastTurn.toX, lastTurn.toY, color);
            }

        } else {
            ClientTurn lastTurn = (ClientTurn) turn;
            PlayerColor color = kbState.playersMap().get(lastTurn.clientId).color;

            switch (lastTurn.type) {
                case PADDOCK, BARN, HARBOR -> moveServerSettlement(lastTurn.x, lastTurn.y, lastTurn.toX, lastTurn.toY, color);
                default -> setServerSettlement(lastTurn.x, lastTurn.y, color);
            }
        }
        // Highlight Terrain only for mainClient
        if (isNextLocalClient(kbState)) {
            highlightTerrain(Game.allBasicTurnTiles(
                    kbState.gameMap(), kbState.playersMap().get(kbState.nextPlayer())));
        }

        // SETTLEMENTS
        List<Player> players = kbState.players();

        if (players.isEmpty()) {
            return;
        }

        for (int i = 0; i < kbState.players().size(); i++) {
            if (players.get(i) != null)
                updateSettlementsForPlayer(i, players.get(i).getRemainingSettlements());
        }

        if (kbState.currentPlayer() != null && kbState.currentPlayer().getCurrentTurnState() == TurnState.END_OF_TURN
            /*&& kbState.mainClient().getClientId() == kbState.currentPlayer().ID*/ && !isSpectating) {
            game_button_end.setDisable(false);
        }

        // Update basic turn label
        basicTurnLeft(kbState);

        // SCORE
        for (int i = 0; i < kbState.players().size(); i++) {
            int score = Game.calculateScore(kbState.gameMap(), kbState.players().get(i),
                    kbState.winConditions(), kbState.players());
            updateScoreForPlayer(i, score);
        }
    }

    /**
     * Shows how many settlements the player has left for their basic turn.
     * @param kbState the current state
     */
    private void basicTurnLeft(KBState kbState) {
        if (kbState.currentPlayer() != null && kbState.currentPlayer().ID == kbState.mainClient().getClientId()) {
            int basicSettlementsLeft = kbState.currentPlayer().getRemainingSettlementsOfTurn();
            final String text = basicSettlementsLeft > 0
                    ? resourceBundle.getString("basicLeft") + " " + basicSettlementsLeft
                    : "";

            game_label_basic.setText(text);
        }
    }

    /**
     * Updates the GUI whenever a token is activated.
     *
     * @param kbState the current state.
     */
    private void onTokenChanged(KBState kbState) {
        if (kbState.currentPlayer() == null)
            return;

        TileType token = kbState.token();
        if (token == null) {
            updateTokens(kbState);

            disableTokens(false);
            highlightTerrain(Game.allBasicTurnTiles(
                    kbState.gameMap(), kbState.playersMap().get(kbState.nextPlayer())));
            if (gameBoard.getMarkedHexagon() != null) {
                gameBoard.getMarkedHexagon().removeMarker();
                gameBoard.markHexagonToMove(null);
            }
            return;
        } else {
            disableTokens(true);
        }

        switch (token) {

            case ORACLE -> highlightTerrain(Game.allTokenOracleTiles(kbState.gameMap(), kbState.currentPlayer()));
            case FARM -> highlightTerrain(Game.allTokenFarmTiles(kbState.gameMap(), kbState.currentPlayer()));
            case TAVERN -> highlightTerrain(Game.allTokenTavernTiles(kbState.gameMap(), kbState.currentPlayer()));
            case TOWER -> highlightTerrain(Game.allTokenTowerTiles(kbState.gameMap(), kbState.currentPlayer()));
            case HARBOR -> highlightTerrain(Game.allTokenHarborTiles(kbState.gameMap(), kbState.currentPlayer(), false));
            case PADDOCK -> highlightTerrain(Game.allTokenPaddockTiles(kbState.gameMap(), kbState.currentPlayer()));
            case BARN -> highlightTerrain(Game.allTokenBarnTiles(kbState.gameMap(), kbState.currentPlayer(), false));
            case OASIS -> highlightTerrain(Game.allTokenOasisTiles(kbState.gameMap(), kbState.currentPlayer()));
            default -> throw new RuntimeException("Tile type is not a token!");
        }
    }

    /**
     * Highlights the given tiles on the board and enables the fog/shadow if any are highlighted.
     * @param tiles the tiles to highlight.
     */
    private void highlightTerrain(Stream<Tile> tiles) {
        boolean tilesAreHighlighted = gameBoard.highlightTerrain(tiles);
        if (shadow != null) {
            if (tilesAreHighlighted) {
                shadow.fadeIn();
            } else {
                shadow.fadeOut();
            }
        }
    }

    /**
     * Updates the GUI whenever the map has changed.
     *
     * @param kbState the current state.
     */
    private void onGameMapChanged(KBState kbState) {
        if (kbState.gameMap() != null && !hasMap) {
            setupGameBoard(kbState.gameMap());
            HexagonTile[][] board = gameBoard.getBoard();
            Point3D minPosition = new Point3D(board[0][0].getTranslateX(), board[0][0].getTranslateY(), MIN_CAMERA_DEPTH);
            Point3D maxPosition = new Point3D(board[19][1].getTranslateX(), board[0][19].getTranslateY(), MAX_CAMERA_DEPTH);
            new GameCamera(game_subscene, minPosition, maxPosition, boardCenter, -300);
            Util.setupLight(gameBoard_group, boardCenter);
            hasMap = true;
        }
    }

    /**
     * Updates the GUI whenever the players of the current game have changed.
     *
     * @param kbState the current state.
     */
    private void onPlayersChanged(KBState kbState) {
        game_hbox_players.getChildren().removeAll(players);
        players.clear();
        if (store.getState().players() != null) {
            int i = 0;
            for (Player player : store.getState().players()) {
                addPlayer(kbState.clients().get(player.ID).name());
                updateSettlementsForPlayer(i++, player.getRemainingSettlements());
            }
        }
    }

    /**
     * Updates the time limit label with the specified values.
     * @param timeRemaining the time remaining in the current turn.
     * @param timeLimit the maximum allowed time of one turn.
     */
    private void setTimeLabel(int timeRemaining, int timeLimit) {
        if (timeLimit > -1) {
            // could also try LocalTime.ofSecondOfDay(timeRemaining).toString()
            timeRemaining = Math.max(timeRemaining, 0);
            String minutesRemaining = String.format("%02d", timeRemaining / 60);
            String secondsRemaining = String.format("%02d", timeRemaining % 60);
            String minutesLimit = String.format("%02d", timeLimit / 60);
            String secondsLimit = String.format("%02d", timeLimit % 60);
            game_label_time.setText(resourceBundle.getObject("timeLimit:") + " "
                    + minutesRemaining + ":" + secondsRemaining + "/" + minutesLimit + ":" + secondsLimit);
        } else {
            game_label_time.setText("");
        }
    }

    /**
     * Updates the GUI whenever the MyGameReply changed (happens only when the game starts).
     *
     * @param kbState the current state.
     */
    private void onMyGameReplyChanged(KBState kbState) {
        MyGameReply myGame = kbState.myGameReply();
        if (myGame != null) {
            // display time limit
            timeLimit = myGame.timeLimit();
            if (timeLimit > -1) {
                timeRemaining = timeLimit = myGame.timeLimit() / 1000;
                setTimeLabel(timeRemaining, timeLimit);
                timeLabelTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                        setTimeLabel(--timeRemaining, timeLimit)
                ));
                timeLabelTimeline.setCycleCount(Animation.INDEFINITE);
            }

            // display turn limit
            if (myGame.turnLimit() != -1)
                game_label_turn.setText(resourceBundle.getObject("turnLimit:")
                        + " " + myGame.turnLimit());
        }
    }

    /**
     * Updates the GUI whenever the win conditions have changed.
     *
     * @param kbState the current state.
     */
    private void onWinConditionsChanged(KBState kbState) {
        if (kbState.winConditions() != null) {
            updateWinConditions();
        }
    }

    /**
     * Updates the GUI whenever the current player has changed.
     *
     * @param kbState the current state.
     */
    private void onCurrentPlayerChanged(KBState kbState) {
        updateTokens(kbState);
    }

    /**
     * Updates the turn (limit) label with the specified values.
     * @param turnCount the current turn.
     * @param turnLimit the maximum allowed turns of the game.
     */
    private void setTurnLabel(int turnCount, int turnLimit) {
        final String text = turnLimit > -1
            ? resourceBundle.getObject("turnLimit:") + " " + turnCount + "/" + turnLimit
            : resourceBundle.getObject("turn:") + " " + turnCount;

        game_label_turn.setText(text);
    }

    /**
     * Updates the GUI whenever the turn count changes.
     *
     * @param kbState the current state.
     */
    private void onTurnCountChanged(KBState kbState) {
        if (kbState.myGameReply() != null) {
            setTurnLabel(kbState.turnCount(), kbState.myGameReply().turnLimit());
        }
    }

    /**
     * Updates the Token Inventory for the current player.
     *
     * @param kbState the current state.
     */
    private void updateTokens(KBState kbState) {
        tokens.clear();
        game_hbox_tokens.getChildren().clear();

        if (isCurrentLocalClient(kbState)) {
            for (var entry : kbState.currentPlayer().getTokens().entrySet()) {
                if (entry.getValue().getTotal() == 0)
                    continue;

                Token token = new Token(entry.getKey(), entry.getValue().getRemaining(), this,
                        areTokensDisabled, resourceBundle, store);

                tokens.add(token);
                game_hbox_tokens.getChildren().add(token);
            }
        }
    }

    /**
     * Sets a settlement that comes from a server message.
     *
     * @param x     the x-coordinate.
     * @param y     the y-coordinate.
     * @param color the color for the settlement.
     */
    private void setServerSettlement(int x, int y, PlayerColor color) {
        gameBoard.placeSettlement(x, y, color);
    }

    /**
     * Updates the terrain card when a new one is drawn.
     *
     * @param kbState the current state.
     */
    private void onTerrainCardChanged(KBState kbState) {
        if (kbState.nextTerrainCard() != null) {
            updateCardDescription(kbState.nextTerrainCard());
        }

        if (kbState.currentPlayer() != null && isNextLocalClient(kbState)) {
                highlightTerrain(Game.allBasicTurnTiles(
                        kbState.gameMap(), kbState.playersMap().get(kbState.nextPlayer())));
        }

        if (timeLimit != -1 && timeLabelTimeline != null) {
            timeLabelTimeline.stop();
            timeRemaining = timeLimit;
            setTimeLabel(timeRemaining, timeLimit);
            timeLabelTimeline.play();
        }
    }

    /**
     * Moves a settlement from one location to another.
     *
     * @param fromX the x-coordinate where the settlement is moved from.
     * @param fromY the y-coordinate where the settlement is moved from.
     * @param toX   the x-coordinate where the settlement is moved to.
     * @param toY   the y-coordinate where the settlement is moved to.
     * @param color the player color.
     */
    private void moveServerSettlement(int fromX, int fromY, int toX, int toY, PlayerColor color) {
        //gameBoard.removeSettlement(fromX, fromY);
        gameBoard.moveAnimation(fromX, fromY, toX, toY, color);
        //gameBoard.placeSettlement(toX, toY, color);
    }

    /**
     * Generates the 20 x 20 field of the hexagons.
     *
     * @param gameMap the map with all information.
     */
    private void setupGameBoard(GameMap gameMap) {
        gameBoard.setupBoard(gameBoard_group, gameMap, resourceBundle);

        HexagonTile[][] board = gameBoard.getBoard();
        boardCenter = new Point3D(
                (board[9][0].getTranslateX() + board[10][1].getTranslateX()) / 2f,
                (board[0][9].getTranslateY() + board[0][10].getTranslateY()) / 2f,
                -Hexagon.HEXAGON_DEPTH
        );

        shadow = new Fog(1650, 1550, 1, 8, Color.BLACK, 0.2, 0.4);

        // the clouds in the background of the scene
        Fog clouds = new Fog(5000, 4000, 1, 16, Color.WHITE);

        // the order of adding these matters for transparent rendering in JavaFX despite usage of a z-buffer
        gameBoard_group.getChildren().add(clouds);
        gameBoard_group.getChildren().add(shadow);

        shadow.setTranslateX(boardCenter.getX());
        shadow.setTranslateY(boardCenter.getY());
        shadow.setTranslateZ(boardCenter.getZ() * 1.3); // above the board

        clouds.setTranslateX(boardCenter.getX());
        clouds.setTranslateY(boardCenter.getY());
        clouds.setTranslateZ(-boardCenter.getZ() * 1.3); // below the board
        clouds.fadeIn();
    }

    /**
     * Changes some functionalities when spectating or playing.
     */
    private void playingOrSpectating() {
        if (isSpectating) {
            game_button_end.setDisable(false);
            game_button_end.setText(resourceBundle.getString("endSpectate"));
            game_button_end.setOnAction(this::onSpectateEndButtonPressed);
        } else {
            game_button_end.setText(resourceBundle.getString("endTurn"));
            game_button_end.setOnAction(this::onTurnEndButtonPressed);
        }
    }

    /**
     * Initialize layout arrangement.
     */
    private void setupLayout() {
        //important: the order in which the bind-operations are called

        // resize the HBox for statistic at top and player information on the bottom
        game_hbox_statistic.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.1));
        game_hBox_windconditions.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.1));
        game_hbox_subscene.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.7));
        game_hbox_playerinformation.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.15));

        //resize VBox that contains the information for the current terrain type (picture, text)
        game_vbox_terraincard.prefWidthProperty().bind(game_vbox.widthProperty().multiply(0.15));
        game_vbox_terraincard.prefHeightProperty().bind(game_hbox_playerinformation.heightProperty().multiply(0.8));

        // resize rectangle that contains the Image for the current terrain type
        game_rectangle_card.widthProperty().bind(game_vbox_terraincard.widthProperty().multiply(0.8));
        game_rectangle_card.heightProperty().bind(game_vbox_terraincard.heightProperty().multiply(0.7));

        // resize the subScene
        game_subscene.widthProperty().bind(game_hbox_subscene.widthProperty());
        game_subscene.heightProperty().bind(game_hbox_subscene.heightProperty());

        // resize the player list for current game
        game_hbox_players.maxWidthProperty().bind(game_vbox.widthProperty().subtract(75));
    }

    /**
     * Updates the Card to the current card of the turn
     *
     * @param tileType the type to show.
     */
    private void updateCardDescription(TileType tileType) {
        //Update Image
        Image img = TextureLoader.getTileTexture(tileType);
        game_rectangle_card.setFill(new ImagePattern(img, 0.0f, 0.0f, 1.0f, 1.0f, true));

        //Update Text
        game_label_carddescribtion.setText(tileType.toStringLocalized());
    }

    /**
     * Disables all tokens except the selected.
     *
     * @param disable if the tokens should be disabled then use true.
     */
    public void disableTokens(boolean disable) {
        if (tokens.size() == 0) {
            return;
        }

        areTokensDisabled = disable;

        // iterate through all Tokens
        for (Token token : tokens) {
            // Skip activated Token
            if (token.isTokenActivated()) {
                continue;
            }

            if (disable) {
                token.disableToken();
            } else {
                token.enableToken();
                token.cancelTokenSelection();
            }
        }
    }

    /**
     * Adds a player to the view.
     *
     * @param name the name of the player.
     */
    private void addPlayer(String name) {
        PlayerColor color = PlayerColor.RED;

        int size = players.size();
        if (size == 0) color = PlayerColor.RED;
        if (size == 1) color = PlayerColor.BLUE;
        if (size == 2) color = PlayerColor.BLACK;
        if (size == 3) color = PlayerColor.WHITE;
        if (size > 3) return;

        game_hbox_players.getChildren().remove(game_vBox_addPlayerVBox);

        boolean colorMode = store.getState().betterColorsActive();
        PlayerInformation player = new PlayerInformation(name, color, colorMode);
        game_hbox_players.getChildren().add(player);

        if (!game_hbox_players.getChildren().contains(game_vBox_addPlayerVBox))
            game_hbox_players.getChildren().add(game_vBox_addPlayerVBox);

        // add spacing
        Region region = new Region();
        game_hbox_players.getChildren().add(region);
        HBox.setHgrow(region, Priority.ALWAYS);


        players.add(player);
    }

    /**
     * Highlights which players turn is it.
     *
     * @param playerList the list of all players in the game.
     * @param current    the current player.
     */
    private void setCurrentPlayerHighlight(ArrayList<Player> playerList, Player current) {
        for (var p : players) {
            p.setHighlight(false);
        }

        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == current)
                players.get(i).setHighlight(true);
        }
    }

    /**
     * Updates the score of a player.
     *
     * @param player the selected player (0-3).
     * @param score  the new score.
     */
    private void updateScoreForPlayer(int player, int score) {
        players.get(player).setScore(score);
    }

    /**
     * Updates the settlements of a player.
     *
     * @param player the selected player (0-3).
     * @param count  the new settlement count.
     */
    private void updateSettlementsForPlayer(int player, int count) {
        players.get(player).setSettlementCount(count);
    }

    /**
     * Updates the win conditions.
     */
    private void updateWinConditions() {
        // should be empty but safe is safe
        game_hBox_windconditions.getChildren().clear();

        List<WinCondition> winConditions = store.getState().winConditions();

        for (WinCondition wc : winConditions) {
            game_hBox_windconditions.getChildren().add(new Wincondition(wc, resourceBundle));
        }
    }

    /**
     * Gets the SubScene for the game board.
     *
     * @return SubScene for displaying the game board
     */
    public SubScene getGame_subscene() {
        return this.game_subscene;
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending the turn.
     *
     * @param actionEvent the triggered event.
     */
    private void onTurnEndButtonPressed(ActionEvent actionEvent) {
        if (store.getState().currentPlayer() != null
                && store.getState().currentPlayer().getCurrentTurnState() == TurnState.END_OF_TURN
                && store.getState().token() == null) {
            store.dispatch(GameReducer.END_TURN, null);
        }
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending spectating
     *
     * @param actionEvent the triggered event.
     */
    private void onSpectateEndButtonPressed(ActionEvent actionEvent) {
        if (isOnline) {
            sceneLoader.showGameSelectionView();
            store.dispatch(GameReducer.UNSPECTATE_GAME, null);
        } else {
            sceneLoader.showMenuView();
        }
    }

    /**
     * Set the spectating value, if the game view is for a spectating game.
     *
     * @param spectating if the game is observed by a spectator.
     */
    public void setSpectating(boolean spectating) {
        this.isSpectating = spectating;
    }

    /**
     * Sets the online value, if the game is on an online server.
     *
     * @param isOnline if the game is an online game.
     */
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
        playingOrSpectating();
    }

    /**
     * @param state the state to checkj against.
     * {@return whether the next turn belongs to a local client.}
     */
    private static boolean isNextLocalClient(KBState state) {
        final boolean isHotSeat = state
            .hotSeatClients()
            .stream()
            .anyMatch(c -> c.getClientId() == state.nextPlayer());

        final boolean isMain = state.nextPlayer() == state.mainClient().getClientId();

        return isMain || isHotSeat;
    }

    /**
     * @param state the state to check against.
     * {@return whether the current turn belongs to a local client.}
     */
    private static boolean isCurrentLocalClient(KBState state) {
        if(state.currentPlayer() == null) return false;

        final boolean isHotSeat = state
            .hotSeatClients()
            .stream()
            .anyMatch(c -> c.getClientId() == state.currentPlayer().ID);

        final boolean isMain = state.currentPlayer().ID == state.mainClient().getClientId();
        return isHotSeat || isMain;
    }

}
