package kingdomBuilder.gui.levelEditor;

import javafx.scene.paint.PhongMaterial;
import kingdomBuilder.gamelogic.Game;
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

    private int x;
    private int y;

    /**
     * Constructs a new Tile with the given parameters.
     *
     * @param xPos the x position.
     * @param yPos the y position.
     * @param tileType the type of the hexagon.
     * @param resource the language support.
     * @param controller the LevelEditorController.
     */
    protected EditorTile(int x, int y, double xPos, double yPos, Game.TileType tileType,
                         ResourceBundle resource, LevelEditorController controller) {
        super(xPos, yPos, null, resource);
        levelEditorController = controller;
    }

    /**
     * Set the mouse handler.
     */
    @Override
    protected void setMouseHandler() {
        setOnMouseClicked(event -> {
            Game.TileType tmp = levelEditorController.getTileTypeFromCombobox();

            tileType = tmp;

            PhongMaterial mat = MaterialLoader.getMaterial(tmp);
            hexagon.setMaterial(mat);
        });
    }

    public Game.TileType getTileType() {
        return tileType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
