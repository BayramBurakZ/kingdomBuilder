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
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.UploadQuadrantAction;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gui.GameCamera;
import kingdomBuilder.gui.controller.Controller;
import kingdomBuilder.gui.gameboard.Hexagon;
import kingdomBuilder.gui.util.Util;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ResourceBundle;

/**
 * Class for the Controller of the LevelEditor.
 */
public class LevelEditorController extends Controller implements Initializable {

    /**
     * Represents the limit for castles in one quadrant defined by the server.
     */
    private static final int CASTLE_LIMIT = 3;

    /**
     * Represents the limit for special places in one quadrant defined by the server.
     */
    private static final int SPECIAL_LIMIT = 3;

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
    public EditorBoard editorBoard = new EditorBoard(this);

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
     * Constructs a new LevelEditorController.
     * @param store the reference to the store.
     */
    public LevelEditorController(Store<KBState> store) {
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
        resourceBundle = resources;
        setupLayout();
        setupBoard();
        setupComboBox();

        new GameCamera(editor_subscene, boardCenter, -750);
        Util.setupLight(editor_board_group, boardCenter);
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
        String types = "";

        for (EditorTile[] row : editorBoard.board) {
            for (EditorTile tile : row) {
                types += tile.getTileType() + ";";
            }
        }

        System.out.println(types);

        store.dispatchOld(new UploadQuadrantAction(types));
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
     * Generates the 10 x 10 field of the hexagons.
     */
    private void setupBoard() {
        editorBoard.setupEmptyQuadrant(editor_board_group, resourceBundle);

        board = editorBoard.getBoard();
        boardCenter = new Point3D(
                (board[4][0].getTranslateX() + board[5][1].getTranslateX()) / 2f,
                (board[0][4].getTranslateY() + board[0][5].getTranslateY()) / 2f,
                Hexagon.HEXAGON_DEPTH
        );

        // required for dragging cursor over tiles to paint
        editor_subscene.setOnDragDetected(mouseEvent -> {
            editor_subscene.startFullDrag();
        });
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
    public TileType getTileTypeFromCombobox() {
        return stringToTileType(editor_comboBox.getSelectionModel().getSelectedItem());
    }

    /**
     * Translates a given string to a tile type.
     *
     * @param tileTypeString string to get translated into a tile type.
     * @return The tile type as enum.
     */
    private TileType stringToTileType(String tileTypeString) {
        if (tileTypeString == null) return null;
        return switch (tileTypeString) {
            case "Canyon" -> TileType.CANYON;
            case "Desert" -> TileType.DESERT;
            case "Flower" -> TileType.FLOWER;
            case "Forest" -> TileType.FORREST;
            case "Gras" -> TileType.GRAS;
            case "Mountain" -> TileType.MOUNTAIN;
            case "Water" -> TileType.WATER;
            case "Castle" -> TileType.CASTLE;
            case "Barn" -> TileType.BARN;
            case "Farm" -> TileType.FARM;
            case "Harbor" -> TileType.HARBOR;
            case "Oasis" -> TileType.OASIS;
            case "Oracle" -> TileType.ORACLE;
            case "Paddock" -> TileType.PADDOCK;
            case "Tavern" -> TileType.TAVERN;
            case "Tower" -> TileType.TOWER;
            default -> throw new InvalidParameterException("String doesn't match to Tile type.");
        };
    }

    /**
     * Checks if all tiles are placed on the map.
     *
     * @return True if all tiles are placed. False otherwise.
     */
    public boolean allTilesArePlaced() {
        int castles = 0;
        int special = 0;

        for (EditorTile[] row : board) {
            for (EditorTile e : row) {
                if(e.getTileType() == null) {
                    System.out.println("Not all Tiles are set!");
                    return false;
                }

                if(e.getTileType() == TileType.CASTLE)
                    castles++;

                if(TileType.tokenType.contains(e.getTileType()))
                    special++;
            }
        }

        // seems the server don't accept quadrants with more than 3 castles or more than 3 special places
        if (castles > CASTLE_LIMIT) {
            System.out.println("Castle Limit is 3!");
            return false;
        }

        if (special > SPECIAL_LIMIT) {
            System.out.println("Special Place Limit is 3!");
            return false;
        }

        return true;
    }
}
