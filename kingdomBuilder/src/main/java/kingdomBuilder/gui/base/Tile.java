package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.gamelogic.TileType;
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
    protected TileType tileType;

    /**
     * Represents the x-coordinate.
     */
    protected final int x;

    /**
     * Represents the y-coordinate.
     */
    protected final int y;

    /**
     * Represents the x position.
     */
    protected final double xPos;

    /**
     * Represents the y position.
     */
    protected final double yPos;

    /**
     * Constructs a new Tile with the given parameters.
     * @param xPos the x position.
     * @param yPos the y position.
     * @param tileType the type of the hexagon.
     * @param resource the language support.
     */
    protected Tile(int x, int y, double xPos, double yPos, TileType tileType, ResourceBundle resource) {
        if (resourceBundle == null || !resourceBundle.equals(resource) || resourceBundle != resource) {
            resourceBundle = resource;
        }

        this.x = x;
        this.y = y;

        this.xPos = xPos;
        this.yPos = yPos;

        // create and add hexagon prism
        hexagon = new Hexagon(tileType);
        getChildren().add(hexagon);

        // set tileType
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
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Gets the x-coordinate.
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     * @return the y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the x position.
     * @return the x position.
     */
    public double getXPos() {
        return xPos;
    }

    /**
     * Gets the y position.
     * @return the y position.
     */
    public double getYPos() {
        return yPos;
    }
}
