package kingdomBuilder.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.gameboard.GameBoard;
import kingdomBuilder.gui.gameboard.*;
import kingdomBuilder.model.Model;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameView.
 */
public class GameViewController extends Controller implements Initializable {
    /**
     * Represents the setting for the field of view (fov).
     */
    private static final double FOV = 50.0;

    /**
     * Represents the angle for the camera.
     */
    private static final double VIEW_ANGLE = 30.0;

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
    private HBox gameview_hbox_tokens;
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

    //endregion FXML

    //TODO: remove with adding subscribers
    // temporary solution to store the board instead using the data from dataLogic
    private TileType[][] gameBoardData;

    /**
     * Represents the gameBoard with data for the gui like hexagons and textures.
     */
    public GameBoard gameBoard = new GameBoard(store);

    /**
     * Represents the center of the gameboard as a Point3D.
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
    private ArrayList<Token> tokens = new ArrayList<Token>();

    /**
     * Represents the list of players on the top of the view.
     */
    private ArrayList<PlayerInformation> players = new ArrayList<>();

    /**
     * Constructs the GameView with the given store.
     * @param store The Store for access to the state.
     */
    public GameViewController(Store<KBState> store) {
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
        //TODO: Subscribers:
        // - PlayerData settlements -> updateSettlementsForPlayer()
        // - PlayerData score -> updateScoreForPlayer()
        // - when player joins -> addPlayer()
        // - Win Conditions -> updateWinConditions()
        // - Tokens from the current Player -> updateTokens()
        // - Token used -> updateTokens()
        // - In Basic turn -> disableTokens(false/true)
        // - current Terrain -> updateCardDescription()
        // - if PlayersTurn -> disableTokens(false / true)

        resourceBundle = resources;

        // TODO: Remove with adding subscribers
        Model model = new Model();
        gameBoardData = model.getGameBoardData();

        setupGameBoard();
        setupCamera();
        setupLight();
        setupLayout();

        // create a testBox
        Box box = new Box(100, 100, 100);
        box.setMaterial(new PhongMaterial(
                Color.WHITE, TextureLoader.getTileTexture(TileType.FARM), null, null, null)
        );
        box.translateXProperty().set(1500);
        box.translateYProperty().set(1500);
        box.setOnMouseEntered(e -> {
            ((PhongMaterial)box.getMaterial()).setDiffuseColor(Color.CHOCOLATE);
        });
        box.setOnMouseExited(e -> {
            ((PhongMaterial)box.getMaterial()).setDiffuseColor(Color.WHITE);
        });
        gameBoard_group.getChildren().add(box);
    }

    /**
     * Sets the initial light for the board.
     */
    private void setupLight() {
        AmbientLight al = new AmbientLight(Color.gray(0.4));
        gameBoard_group.getChildren().add(al);

        // for some reason JavaFX doesn't support vector light/sunlight
        SpotLight sl = new SpotLight(Color.gray(0.6));
        gameBoard_group.getChildren().add(sl);

        sl.setTranslateX(boardCenter.getX());
        sl.setTranslateY(boardCenter.getY());
        sl.setTranslateZ(-7000);
    }

    /**
     * Initializes the Camera for the subScene.
     */
    private void setupCamera() {
        // fixedEyeAtCameraZero has to be true or a change in the window's aspect ratio modifies the FOV
        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setFarClip(4096.0);
        camera.setRotationAxis(new Point3D(1.0, 0, 0));
        camera.setRotate(VIEW_ANGLE);
        camera.setFieldOfView(FOV);
        game_subscene.setCamera(camera);


        // TODO: set initial camera position properly
        camera.setTranslateX(boardCenter.getX());
        camera.setTranslateY(
                (1 + Math.sin(Math.toRadians(VIEW_ANGLE)) + Math.sin(Math.toRadians(FOV))) * boardCenter.getY());
        camera.setTranslateZ(-Math.cos(Math.toRadians(VIEW_ANGLE)) * gameBoard.getBoard()[19][0].getTranslateX());

        setupCameraHandlers(camera);
    }

    /**
     * Setup all connected EventHandler.
     * @param camera The camera for the handlers.
     */
    private void setupCameraHandlers(Camera camera) {
        setupCameraZoomHandler(camera);
        setupCameraScrollHandler(camera);
    }

    /**
     * Zooms the camera when the user scrolls the mousewheel.
     * @param camera The camera for zooming.
     */
    private void setupCameraZoomHandler(Camera camera) {
        game_subscene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();

            double zoomSpeed = 1.5;

            double translation = deltaY * zoomSpeed;

            Point3D pos = camera.localToScene(0, 0, translation);
            camera.setTranslateX(pos.getX());
            camera.setTranslateY(pos.getY());
            camera.setTranslateZ(pos.getZ());

            event.consume();
        });
    }

    /**
     * Translates the camera when the user presses the arrow keys.
     * @param camera The camera to move.
     */
    private void setupCameraScrollHandler(Camera camera) {
        // TODO: smoother scrolling
        game_subscene.setOnKeyPressed((KeyEvent event) -> {
            double scrollSpeed = 20.0;
            switch (event.getCode()) {
                case UP -> camera.setTranslateY(camera.getTranslateY() - scrollSpeed);
                case DOWN -> camera.setTranslateY(camera.getTranslateY() + scrollSpeed);
                case LEFT -> camera.setTranslateX(camera.getTranslateX() - scrollSpeed);
                case RIGHT -> camera.setTranslateX(camera.getTranslateX() + scrollSpeed);
                //ToDO: remove - just for testing the highlight
                case R -> gameBoard.highlightTerrain();
                case T -> {
                    int random = (int) (Math.random() * 5) + 1;
                    updateCardDescription(TileType.valueOf(random));
                    updateTokens();
                }
                case Z -> updateWinConditions();
                case P -> addPlayer("Tom44");

            }
            event.consume();
        });
    }

    /**
     * Generates the 20 x 20 field of the hexagons.
     */
    private void setupGameBoard() {
        // TODO: access data via state
        gameBoard.setupBoard(gameBoard_group, gameBoardData, resourceBundle);

        HexagonTile[][] board = gameBoard.getBoard();
        boardCenter = new Point3D(
                (board[9][0].getTranslateX() + board[10][0].getTranslateX()) / 2f,
                (board[0][9].getTranslateY() + board[0][10].getTranslateY()) / 2f,
                Hexagon.HEXAGON_DEPTH
        );
    }

    /**
     * Changes some functionalities when spectating or playing.
     */
    private void playingOrSpectating() {
        if (isSpectating) {
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

        game_hbox_players.maxWidthProperty().bind(game_vbox.widthProperty().subtract(75));
    }

    /**
     * Updates the Card to the current card of the turn
     * @param tileType The type to show.
     */
    private void updateCardDescription(TileType tileType) {
        // TODO read TileType from Datalogic/Store instead of parameter

        //Update Image
        Image img = TextureLoader.getTileTexture(tileType);
        game_rectangle_card.setFill(new ImagePattern(img, 0.0f, 0.0f, 1.0f, 1.0f, true));

        //Update Text
        String cardDescription = "";
        switch(tileType) {
            case GRAS -> cardDescription = resourceBundle.getString("gras");
            case FLOWER -> cardDescription = resourceBundle.getString("flower");
            case DESERT -> cardDescription = resourceBundle.getString("desert");
            case CANYON -> cardDescription = resourceBundle.getString("canyon");
            case FORREST -> cardDescription = resourceBundle.getString("forrest");
        }
        game_label_carddescribtion.setText(cardDescription);
    }

    /**
     * Disables all tokens except the selected.
     * @param disable If the tokens should be disabled then use true.
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
            }
        }
    }

    /**
     * Updates the token bar at the bottom of the screen.
     */
    private void updateTokens() {
        //tokens.clear();
        //gameview_hbox_tokens.getChildren().clear();

        // TODO: read information from Datalogic which Tokens are necessary
        //  instead of using a randomizer and the for loop
        //  and clear tokens and hbox first (2 lines above)

        int random = (int) (Math.random() * 8) + 9;
        int count = 2;

        TileType tileType = TileType.valueOf(random);

        //gameview_hbox_tokens
        Token token = new Token(tileType, count, this, areTokensDisabled, resourceBundle);
        tokens.add(token);

        gameview_hbox_tokens.getChildren().add(token);
    }

    /**
     * Adds a player to the view.
     * @param name The name of the player.
     */
    private void addPlayer(String name) {
        // TODO: adjust to gamelogic or read from state
        PlayerInformation.PlayerColor color = PlayerInformation.PlayerColor.RED;

        int size = players.size();
        if (size == 0)  color = PlayerInformation.PlayerColor.RED;
        if (size == 1)  color = PlayerInformation.PlayerColor.BLUE;
        if (size == 2)  color = PlayerInformation.PlayerColor.BLACK;
        if (size == 3)  color = PlayerInformation.PlayerColor.WHITE;
        if (size > 3)   return;

        boolean colorMode = store.getState().betterColorsActiv;
        PlayerInformation player = new PlayerInformation(name, color, colorMode);
        game_hbox_players.getChildren().add(player);

        // add spacing
        Region region = new Region();
        game_hbox_players.getChildren().add(region);
        HBox.setHgrow(region, Priority.ALWAYS);
        
        players.add(player);
    }

    /**
     * Updates the score of a player.
     *
     * @param player The selected player (0-3).
     * @param score The new score.
     */
    private void updateScoreForPlayer(int player, int score) {
        players.get(player).setScore(score);
    }

    /**
     * Updates the settlements of a player.
     *
     * @param player The selected player (0-3).
     * @param count The new settlement count.
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

        // TODO: read winconditions from Data
        Wincondition.WinCondition[] wc = {
                Wincondition.WinCondition.FISHER,
                Wincondition.WinCondition.LORDS,
                Wincondition.WinCondition.MINER,
                Wincondition.WinCondition.ANCHORITE,
                Wincondition.WinCondition.FARMER,
                Wincondition.WinCondition.MERCHANT,
                Wincondition.WinCondition.KNIGHT,
                Wincondition.WinCondition.EXPLORER
        };

        for (int i = 0; i < 3; i++) {
            int random = (int) (Math.random() * 8);
            game_hBox_windconditions.getChildren().add(new Wincondition(wc[random], resourceBundle));
        }

        /*
        Wincondition one = new Wincondition(winConditionOne, resourceBundle);
        Wincondition two = new Wincondition(winConditionTwo, resourceBundle);
        Wincondition three = new Wincondition(winConditionThree, resourceBundle);

        game_hBox_windconditions.getChildren().addAll(one, two, three);
        */
    }

    /**
     * Gets the SubScene for the game board.
     * @return SubScene for displaying the game board
     */
    public SubScene getGame_subscene() {
        return this.game_subscene;
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending the turn.
     * @param actionEvent the triggered event.
     */
    public void onTurnEndButtonPressed(ActionEvent actionEvent) {
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending spectating
     * @param actionEvent the triggered event.
     */
    public void onSpectateEndButtonPressed(ActionEvent actionEvent) {
        // TODO: Network Message "unspectate"
        if (isOnline) {
            sceneLoader.showGameSettingsView(true);
        } else {
            // TODO: Network End internal server
            sceneLoader.showMenuView();
        }
    }

    /**
     * Set the spectating value, if the game view is for a spectating game.
     * @param spectating If the game is observed by a spectator.
     */
    public void setSpectating(boolean spectating) {
        this.isSpectating = spectating;
    }

    /**
     * Sets the online value, if the game is on an online server.
     * @param isOnline If the game is an online game.
     */
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
        playingOrSpectating();
    }
}
