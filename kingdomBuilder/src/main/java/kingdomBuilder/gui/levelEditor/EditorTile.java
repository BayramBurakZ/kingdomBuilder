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
        setOnMouseClicked(event -> {
            TileType tmp = levelEditorController.getTileTypeFromCombobox();

            tileType = tmp;

            PhongMaterial mat = MaterialLoader.getMaterial(tmp);
            hexagon.setMaterial(mat);
        });

        /*
        // failed attempt to paint the terrain instead of clicking everytime

        setOnMouseEntered(event -> {
            if (event.isPrimaryButtonDown())
                System.out.println("links");
            if (event.isSecondaryButtonDown())
                System.out.println("rechts");

            if (event.isPrimaryButtonDown()) {
                Game.TileType tmp = levelEditorController.getTileTypeFromCombobox();

                tileType = tmp;

                PhongMaterial mat = MaterialLoader.getMaterial(tmp);
                hexagon.setMaterial(mat);
            }
        });
        */

    }
}
