package kingdomBuilder.gui.levelEditor;

import javafx.scene.paint.PhongMaterial;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gui.base.Tile;
import kingdomBuilder.gui.gameboard.MaterialLoader;

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
     * @param xPos the x position.
     * @param yPos the y position.
     * @param tileType the type of the hexagon.
     * @param resource the language support.
     * @param controller the LevelEditorController.
     */
    protected EditorTile(int x, int y, double xPos, double yPos, TileType tileType,
                         ResourceBundle resource, LevelEditorController controller) {
        super(x, y, xPos, yPos, null, resource);
        levelEditorController = controller;
    }

    /**
     * Set the mouse handler.
     */
    @Override
    protected void setMouseHandler() {
        setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            TileType tmp = levelEditorController.getTileTypeFromCombobox();
            if (tmp != null)  {
                tileType = tmp;
                PhongMaterial mat = MaterialLoader.getMaterial(tmp);
                hexagon.setMaterial(mat);
            }
        });

        // requires startFullDrag() in containing scene's OnDragDetected event
        setOnMouseDragEntered(mouseDragEvent -> {
            if (!mouseDragEvent.isPrimaryButtonDown()) {
                return;
            }

            TileType tmp = levelEditorController.getTileTypeFromCombobox();
            if (tmp != null)  {
                tileType = tmp;
                PhongMaterial mat = MaterialLoader.getMaterial(tmp);
                hexagon.setMaterial(mat);
            }
        });
    }
}
