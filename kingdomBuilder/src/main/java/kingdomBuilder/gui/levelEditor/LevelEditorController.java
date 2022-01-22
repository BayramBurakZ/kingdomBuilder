package kingdomBuilder.gui.levelEditor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ComboBox;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gui.controller.Controller;
import kingdomBuilder.gui.gameboard.Hexagon;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ResourceBundle;

/**
 * Class for the Controller of the LevelEditor.
 */
public class LevelEditorController extends Controller implements Initializable {

    /**
     * Represents the setting for the field of view (fov).
     */
    private static final double FOV = 50.0;

    /**
     * Represents the angle for the camera.
     */
    private static final double VIEW_ANGLE = 30.0;

    /**
     * Represents the root hBox.
     */
    @FXML
    private HBox editor_hBox;

    /**
     * Represents the VBox of the SubScene.
     */
    @FXML
    private VBox editor_vbox_subscene;

    /**
     * Represents the SubScene.
     */
    @FXML
    private SubScene editor_subscene;

    /**
     * Represents the Group inside the SubScene.
     */
    @FXML
    private Group editor_board_group;

    /**
     * Represents the VBox for the Settings.
     */
    @FXML
    private VBox editor_vBox_settings;

    /**
     * Represents the ComboBox to select the Tile.
     */
    @FXML
    private ComboBox<String> editor_comboBox;

    /**
     * Represents the gameBoard with data for the gui like hexagons and textures.
     */
    public EditorBoard editorBoard = new EditorBoard(store, this);

    /**
     * Represents the center of the gameboard as a Point3D.
     */
    private Point3D boardCenter;

    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

    /**
     * Represents the board.
     */
    private EditorTile[][] board;


    /**
     * Called to initialize this controller after its root element has been completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        setupLayout();
        setupBoard();
        setupComboBox();

        setupCamera();
        setupLight();
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
        editor_subscene.setCamera(camera);


        // TODO: set initial camera position properly
        camera.setTranslateX(boardCenter.getX());
        camera.setTranslateY(
                (1 + Math.sin(Math.toRadians(VIEW_ANGLE)) + Math.sin(Math.toRadians(FOV))) * boardCenter.getY());
        camera.setTranslateZ(-Math.cos(Math.toRadians(VIEW_ANGLE)) * board[9][0].getTranslateX());

        setupCameraZoomHandler(camera);
    }

    /**
     * Zooms the camera when the user scrolls the mousewheel.
     * @param camera the camera for zooming.
     */
    private void setupCameraZoomHandler(Camera camera) {
        editor_subscene.setOnScroll((ScrollEvent event) -> {
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
     * Sets the initial light for the board.
     */
    private void setupLight() {
        AmbientLight al = new AmbientLight(Color.gray(0.4));
        editor_board_group.getChildren().add(al);

        // for some reason JavaFX doesn't support vector light/sunlight
        SpotLight sl = new SpotLight(Color.gray(0.6));
        editor_board_group.getChildren().add(sl);

        sl.setTranslateX(boardCenter.getX());
        sl.setTranslateY(boardCenter.getY());
        sl.setTranslateZ(-7000);
    }

    /**
     * Represents the functions for the "Cancel"-Button.
     */
    @FXML
    private void onCancelButtonPressed() {
        sceneLoader.showGameSelectionView();
    }

    /**
     * Represents the functions for the "Upload"-Button.
     */
    @FXML
    private void onUploadButtonPressed() {
        if (!allTilesArePlaced()) {
            // TODO: Error Message
            return;
        }
        else {
            uploadQuadrant();
            sceneLoader.showGameSelectionView();
        }
    }

    /**
     * Uploads the Quadrant.
     */
    private void uploadQuadrant() {
        System.out.println("Simulated upload complete!");
        //TODO: implement

        //TODO: Network: send Message "upload"
    }

    /**
     * Initialize layout arrangement.
     */
    private void setupLayout() {
        editor_vbox_subscene.prefHeightProperty().bind(editor_hBox.heightProperty());
        editor_vbox_subscene.prefWidthProperty().bind(editor_hBox.widthProperty().multiply(0.7));

        editor_subscene.heightProperty().bind(editor_vbox_subscene.heightProperty());
        editor_subscene.widthProperty().bind(editor_vbox_subscene.widthProperty());

        editor_vBox_settings.prefWidthProperty().bind(editor_hBox.widthProperty().multiply(0.3));
    }

    /**
     * Generates the 20 x 20 field of the hexagons.
     */
    private void setupBoard() {
        // TODO: access data via state
        editorBoard.setupEmptyQuadrant(editor_board_group, resourceBundle);

        board = editorBoard.getBoard();
        boardCenter = new Point3D(
                (board[4][0].getTranslateX() + board[5][0].getTranslateX()) / 2f,
                (board[0][4].getTranslateY() + board[0][5].getTranslateY()) / 2f,
                Hexagon.HEXAGON_DEPTH
        );
    }

    /**
     * Setup combo box with tile names.
     */
    private void setupComboBox() {

        editor_comboBox.getItems().addAll(
                "Canyon",
                "Desert",
                "Flower",
                "Forest",
                "Gras",
                "Mountain",
                "Water",
                "Castle",
                "Barn",
                "Farm",
                "Harbor",
                "Oasis",
                "Oracle",
                "Paddock",
                "Tavern",
                "Tower"
        );
    }

    /**
     * Get the Tile type from combo box.
     *
     * @return The tile type from combo box.
     */
    public Game.TileType getTileTypeFromCombobox() {
        return stringToTileType(editor_comboBox.getSelectionModel().getSelectedItem());
    }

    /**
     * Translates a given string to a tile type.
     *
     * @param tileTypeString string to get translated into a tile type.
     * @return The tile type as enum.
     */
    private Game.TileType stringToTileType(String tileTypeString) {
        switch (tileTypeString) {
            case "Canyon":
                return Game.TileType.CANYON;
            case "Desert":
                return Game.TileType.DESERT;
            case "Flower":
                return Game.TileType.FLOWER;
            case "Forest":
                return Game.TileType.FORREST;
            case "Gras":
                return Game.TileType.GRAS;
            case "Mountain":
                return Game.TileType.MOUNTAIN;
            case "Water":
                return Game.TileType.WATER;
            case "Castle":
                return Game.TileType.CASTLE;
            case "Barn":
                return Game.TileType.BARN;
            case "Farm":
                return Game.TileType.FARM;
            case "Harbor":
                return Game.TileType.HARBOR;
            case "Oasis":
                return Game.TileType.OASIS;
            case "Oracle":
                return Game.TileType.ORACLE;
            case "Paddock":
                return Game.TileType.PADDOCK;
            case "Tavern":
                return Game.TileType.TAVERN;
            case "Tower":
                return Game.TileType.TOWER;
            default:
                throw new InvalidParameterException("String doesn't match to Tile type.");
        }
    }

    /**
     * Checks if all tiles are placed on the map.
     *
     * @return True if all tiles are placed. False otherwise.
     */
    public boolean allTilesArePlaced() {
        for (EditorTile[] row : board) {
            for (EditorTile e : row) {
                if(e.getTileType() == null)
                    return false;
            }
        }
        return true;
    }
}
