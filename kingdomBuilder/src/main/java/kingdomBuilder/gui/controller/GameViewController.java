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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import kingdomBuilder.gui.gameboard.GameBoard;
import kingdomBuilder.gui.gameboard.TextureLoader;
import kingdomBuilder.model.Model;
import kingdomBuilder.model.TileType;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameView.
 */
public class GameViewController extends Controller implements Initializable {
    /**
     * Represents the texture loader which all hexagons share.
     */
    private static final TextureLoader textureLoader = new TextureLoader();

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
     * Represents the Label for the name of player 1.
     */
    @FXML
    private Label game_label_player1_name;
    /**
     * Represents the Label for the settlements of player 1.
     */
    @FXML
    private Label game_label_player1_settlements;
    /**
     * Represents the Label for the score of player 1.
     */
    @FXML
    private Label game_label_player1_score;
    /**
     * Represents the Label for the name of player 2.
     */
    @FXML
    private Label game_label_player2_name;
    /**
     * Represents the Label for the settlements of player 2.
     */
    @FXML
    private Label game_label_player2_settlements;
    /**
     * Represents the Label for the score of player 2.
     */
    @FXML
    private Label game_label_player2_score;
    /**
     * Represents the Label for the name of player 3.
     */
    @FXML
    private Label game_label_player3_name;
    /**
     * Represents the Label for the settlements of player 3.
     */
    @FXML
    private Label game_label_player3_settlements;
    /**
     * Represents the Label for the score of player 3.
     */
    @FXML
    private Label game_label_player3_score;
    /**
     * Represents the Label for the name of player 4.
     */
    @FXML
    private Label game_label_player4_name;
    /**
     * Represents the Label for the settlements of player 4.
     */
    @FXML
    private Label game_label_player4_settlements;
    /**
     * Represents the Label for the score of player 4.
     */
    @FXML
    private Label game_label_player4_score;

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

    @FXML
    private PointLight gameBoard_pointlight;

    //endregion FXML

    //TODO:
    // temporary solution to store the board instead using the data from dataLogic
    private TileType[][] gameBoardData;

    /**
     * Represents the gameBoard with data for the gui like hexagons and textures.
     */
    private GameBoard gameBoard = new GameBoard(store);

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
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        // TODO: Remove
        Model model = new Model();
        gameBoardData = model.getGameBoardData();

        setupGameBoard();

        setupCamera();
        setupLight();

        setupLayout();

        // create a testBox
        Box box = new Box(100, 100, 100);
        TextureLoader textureLoader = new TextureLoader();
        box.setMaterial(new PhongMaterial(
                Color.WHITE, textureLoader.getTexture(TileType.FARM), null, null, null)
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

    private void setupLight() {
        // TODO: fix light
        double viewAngle = 30.0;
        double fov = 50.0;
        AmbientLight al = new AmbientLight();
        gameBoard_group.getChildren().add(al);

        gameBoard_pointlight.setRotationAxis(new Point3D(1.0, 0, 0));
        gameBoard_pointlight.setRotate(viewAngle);

        gameBoard_pointlight.setTranslateX(
                (gameBoard.gameBoard[9][0].getTranslateX() + gameBoard.gameBoard[10][0].getTranslateX()) / 2f);
        gameBoard_pointlight.setTranslateY(
                (1 + Math.sin(Math.toRadians(viewAngle)) + Math.sin(Math.toRadians(fov))) *
                        (gameBoard.gameBoard[0][9].getTranslateY() + gameBoard.gameBoard[0][10].getTranslateY()) / 2f);
        gameBoard_pointlight.setTranslateZ(-Math.cos(Math.toRadians(viewAngle)) * gameBoard.gameBoard[19][0].getTranslateX());
        System.out.println(gameBoard_pointlight.getTranslateX());
    }

    /**
     * Initializes the Camera for the subScene.
     */
    private void setupCamera() {
        // TODO constants?
        double viewAngle = 30.0;
        double fov = 50.0;

        // fixedEyeAtCameraZero has to be true or a change in the window's aspect ratio modifies the FOV
        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setFarClip(4096.0);
        camera.setRotationAxis(new Point3D(1.0, 0, 0));
        camera.setRotate(viewAngle);
        camera.setFieldOfView(fov);
        game_subscene.setCamera(camera);

        // TODO: set initial camera position properly
        camera.setTranslateX(
                (gameBoard.gameBoard[9][0].getTranslateX() + gameBoard.gameBoard[10][0].getTranslateX()) / 2f);
        camera.setTranslateY(
                (1 + Math.sin(Math.toRadians(viewAngle)) + Math.sin(Math.toRadians(fov))) *
                        (gameBoard.gameBoard[0][9].getTranslateY() + gameBoard.gameBoard[0][10].getTranslateY()) / 2f);
        camera.setTranslateZ(-Math.cos(Math.toRadians(viewAngle)) * gameBoard.gameBoard[19][0].getTranslateX());

        // TODO: for some reason moving the camera causes bugs, so switch to moving the world instead I guess?
        setupCameraHandlers(camera, false);
    }

    /**
     * Setup all connected EventHandler.
     */
    private void setupCameraHandlers(Node node, boolean moveWorld) {
        setupCameraZoomHandler(node, moveWorld);
        setupCameraScrollHandler(node, moveWorld);
    }

    /**
     * Zooms the camera when the user scrolls the mousewheel.
     */
    private void setupCameraZoomHandler(Node node, boolean moveWorld) {
        game_subscene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();

            double zoomSpeed = 1.5;
            zoomSpeed *= moveWorld ? -1.0 : 1.0;

            double translation = deltaY * zoomSpeed;

            if (moveWorld) {
                node.setTranslateZ(node.getTranslateZ() + translation);
            } else {
                Point3D pos = node.localToScene(0, 0, translation);
                node.setTranslateX(pos.getX());
                node.setTranslateY(pos.getY());
                node.setTranslateZ(pos.getZ());
            }

            event.consume();
        });
    }

    /**
     * Translates the camera when the user presses the arrow keys or drags the mouse with a rightclick.
     */
    private void setupCameraScrollHandler(Node node, boolean moveWorld) {
        // TODO: smoother scrolling
        game_subscene.setOnKeyPressed((KeyEvent event) -> {
            double scrollSpeed = 20.0;
            scrollSpeed *= moveWorld ? -1.0 : 1.0;
            switch (event.getCode()) {
                case UP -> node.setTranslateY(node.getTranslateY() - scrollSpeed);
                case DOWN -> node.setTranslateY(node.getTranslateY() + scrollSpeed);
                case LEFT -> node.setTranslateX(node.getTranslateX() - scrollSpeed);
                case RIGHT -> node.setTranslateX(node.getTranslateX() + scrollSpeed);
                //ToDO: remove - just for testing the highlight
                case R ->  gameBoard.highlightTerrain();
            }
            event.consume();
        });
    }

    /**
     * Generates the 20 x 20 field of the hexagons.
     */
    private void setupGameBoard() {
        // TODO: access data via state
        gameBoard.setupGameBoard(gameBoard_group, gameBoardData, resourceBundle);
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
    }

    /**
     * Updates the Card to the current card of the turn
     * @param tileType
     */
    private void cardDescription(TileType tileType) {
        // TODO read TileType from Datalogic/Store instead of parameter

        //Update Image
        Image img = textureLoader.getTexture(tileType);
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
