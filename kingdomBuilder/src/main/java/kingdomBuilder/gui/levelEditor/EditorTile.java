package kingdomBuilder.gui.levelEditor;

import javafx.scene.paint.PhongMaterial;
import kingdomBuilder.gui.base.Tile;
import kingdomBuilder.gui.gameboard.MaterialLoader;
import kingdomBuilder.model.TileType;

import java.util.ResourceBundle;

/**
 * Class to create a Tile for the editor.
 */
public class EditorTile extends Tile {

    /**
     * Represents the LevelEditorController.
     */
    private LevelEditorController levelEditorController;

    /**
     * Constructs a new Tile with the given parameters.
     *
     * @param xPos     The x position.
     * @param yPos     The y position.
     * @param tileType The type of the hexagon.
     * @param resource The language support.
     * @param controller The LevelEditorController.
     */
    protected EditorTile(double xPos, double yPos, TileType tileType, ResourceBundle resource, LevelEditorController controller) {
        super(xPos, yPos, null, resource);
        levelEditorController = controller;
    }

    /**
     * Set the mouse handler.
     */
    @Override
    protected void setMouseHandler() {
        setOnMouseClicked(event -> {
            TileType tmp = levelEditorController.getTileTypeFromCombobox();

            tileType = tmp;

            PhongMaterial mat = MaterialLoader.getMaterial(tmp);
            hexagon.setMaterial(mat);
        });
    }
}
