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
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.UploadQuadrantAction;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gui.GameCamera;
import kingdomBuilder.gui.controller.BotDifficulty;
import kingdomBuilder.gui.controller.Controller;
import kingdomBuilder.gui.gameboard.Hexagon;
import kingdomBuilder.gui.gameboard.HexagonTile;
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
     * Represents the minimum depth the camera is allowed to zoom away from the board.
     */
    public static final int MIN_CAMERA_DEPTH = -1250;

    /**
     * Represents the maximum depth the camera is allowed to zoom towards the board.
     */
    public static final int MAX_CAMERA_DEPTH = -250;

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
    private ComboBox<TileType> editor_comboBox;

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

        EditorTile[][] board = editorBoard.getBoard();
        Point3D minPosition = new Point3D(board[0][0].getTranslateX(), board[0][0].getTranslateY(), MIN_CAMERA_DEPTH);
        Point3D maxPosition = new Point3D(board[9][1].getTranslateX(), board[0][9].getTranslateY(), MAX_CAMERA_DEPTH);
        new GameCamera(editor_subscene, minPosition, maxPosition, boardCenter, -750);
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
            Util.showLocalizedPopupMessage("notAllTilesPlaced", (Stage) sceneLoader.getScene().getWindow());
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
                TileType.CANYON,
                TileType.DESERT,
                TileType.FLOWER,
                TileType.FORREST,
                TileType.GRAS,
                TileType.MOUNTAIN,
                TileType.WATER,
                TileType.CASTLE,
                TileType.BARN,
                TileType.FARM,
                TileType.HARBOR,
                TileType.OASIS,
                TileType.ORACLE,
                TileType.PADDOCK,
                TileType.TAVERN,
                TileType.TOWER
        );

        editor_comboBox.setConverter(new StringConverter<TileType>() {
            @Override
            public String toString(TileType object) {
                return object.toStringLocalized();
            }

            @Override
            public TileType fromString(String string) {
                return null;
            }
        });

        editor_comboBox.getSelectionModel().selectFirst();
    }

    /**
     * Get the Tile type from combo box.
     *
     * @return The tile type from combo box.
     */
    public TileType getTileTypeFromCombobox() {
        return editor_comboBox.getSelectionModel().getSelectedItem();
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
