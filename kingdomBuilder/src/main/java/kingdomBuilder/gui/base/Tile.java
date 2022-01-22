package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gui.gameboard.Hexagon;

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
    protected Game.TileType tileType;

    /**
     * Constructs a new Tile with the given parameters.
     * @param xPos the x position.
     * @param yPos the y position.
     * @param tileType the type of the hexagon.
     * @param resource the language support.
     */
    protected Tile(double xPos, double yPos, Game.TileType tileType, ResourceBundle resource) {
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


        setMouseHandler();
    }

    /**
     * Sets the MouseHandler of a tile.
     */
    protected abstract void setMouseHandler();

    /**
     * Gets the type of this Tile.
     * @return The type.
     */
    public Game.TileType getTileType() {
        return tileType;
    }
}
