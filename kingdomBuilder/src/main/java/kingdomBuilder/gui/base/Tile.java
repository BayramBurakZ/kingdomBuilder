package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.gui.gameboard.Hexagon;
import kingdomBuilder.model.TileType;

import java.util.ResourceBundle;

/**
 * Abstract class that represent a Tile.
 */
public abstract class Tile extends Group {
    /**
     * Represents the resourceBundle that used for language support.
     */
    protected static ResourceBundle resourceBundle;

    /**
     * Represents the hexagon prism.
     */
    protected Hexagon hexagon;

    /**
     * Represents the tileType of the hexagon.
     */
    protected TileType tileType;

    /**
     * Constructs a new Tile with the given parameters.
     * @param xPos The x position.
     * @param yPos The y position.
     * @param tileType The type of the hexagon.
     * @param resource The language support.
     */
    protected Tile(double xPos, double yPos,  TileType tileType, ResourceBundle resource) {
        if (resourceBundle == null || !resourceBundle.equals(resource) || resourceBundle != resource) {
            resourceBundle = resource;
        }

        // create and add hexagon prism
        hexagon = new Hexagon(tileType);
        getChildren().add(hexagon);

        // set tileType
        // TODO use Datalogic enum
        this.tileType = tileType;

        // move this group to new position
        setTranslateX(xPos);
        setTranslateY(yPos);
    }

    /**
     * Sets the MouseHandler of a tile.
     */
    protected abstract void setMouseHandler();
}
