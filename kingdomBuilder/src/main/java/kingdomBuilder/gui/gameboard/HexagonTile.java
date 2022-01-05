package kingdomBuilder.gui.gameboard;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import kingdomBuilder.model.TileType;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class that is used to display the hexagon tiles in the UI.
 */
public class HexagonTile extends Polygon {
    /**
     * Represents the angle in degree to turn the corners around the center;
     */
    private static final int TURN_ANGLE_DEGREE = 60;

    /**
     * Represents the angle in radian to turn the corners around the center;
     */
    private static final double TURN_ANGLE_RADIAN = Math.toRadians(TURN_ANGLE_DEGREE);

    /**
     * Represents the number of corners of a hexagon.
     */
    private static final int NUMBER_OF_CORNERS = 6;

    /**
     * Represents the texture loader which all hexagons share.
     */
    private static final TextureLoader textureLoader = new TextureLoader();

    /**
     * Represents the resourceBundle that used for language support.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Represents the six corners of a hexagon.
     */
    private static ArrayList<Point> corners;

    /**
     * Represents, if the tile is highlighted.
     */
    private boolean highlight = false;

    /**
     * Represents the tileType of th hexagon.
     */
    private TileType tileType;

    /**
     * Creates a new Hexagon Tile at the given position with given Type.
     * @param xPos The x-coordinate of the upper-left corner position.
     * @param yPos The y-coordinate of the upper-left corner position.
     * @param tileType The TileType of the Hexagon.
     * @param resource The ResourceBundle to translate text.
     */
    public HexagonTile(double xPos, double yPos, TileType tileType, ResourceBundle resource) {
        // only calculate the corners once and not for every hexagon
        if (corners == null) {
            //ToDo: extract the hard coded radius
            corners = calculateCorners(40);
        }

        if (resourceBundle == null || !resourceBundle.equals(resource) || resourceBundle != resource) {
            resourceBundle = resource;
        }

        // calculate all corners of a hexagon
        for (int i = 0; i < NUMBER_OF_CORNERS; i++) {
            getPoints().add(xPos + corners.get(i).getX());
            getPoints().add(yPos + corners.get(i).getY());
        }

        this.tileType = tileType;

        // TODO: execute only for Tokens
        setTokenTooltip(tileType);

        setTexture(textureLoader.getTexture(tileType));
        setHexagonStroke(tileType);
        setMouseHandler(tileType);
    }

    /**
     * Calculates the corners of a hexagon with the given radius.
     * @param radius The radius from the center of a hexagon to one of its corners.
     * @return A list with six points of a hexagon.
     */
    private ArrayList<Point> calculateCorners(int radius) {
        ArrayList<Point> corners = new ArrayList<>();

        int x = 0, my = 0;
        int y = my + radius;

        corners.add(new Point(x, y));

        // Rotation matrix to rotate the corners 60 degree around the center
        for (int i = 0; i < NUMBER_OF_CORNERS - 1; i++) {
            int movedX = (int) Math.round( (x * Math.cos(TURN_ANGLE_RADIAN)) - (y * Math.sin(TURN_ANGLE_RADIAN)) );
            int movedY = (int) Math.round( (x * Math.sin(TURN_ANGLE_RADIAN)) + (y * Math.cos(TURN_ANGLE_RADIAN)) );

            corners.add(new Point(movedX, movedY));
            x = movedX;
            y = movedY;
        }

        // search the distance from the center to the far left side
        int minX = Integer.MAX_VALUE;
        for (Point e : corners) {
            if (e.getX() < minX) {
                minX = e.getX();
            }
        }

        //translate the hexagon corners to positive values
        for (int i = 0; i < corners.size(); i++) {
            corners.get(i).translateX(-minX);
            corners.get(i).translateY(radius);
        }
        return corners;
    }

    /**
     * Sets the texture with the given Image.
     * @param texture The Image for the texture.
     */
    private void setTexture(Image texture) {
        setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
    }

    /**
     * Sets the Stroke of a hexagon based on its type.
     * @param tileType The type of the hexagon.
     */
    private void setHexagonStroke(TileType tileType) {
        // TODO: Adjust to gameLogic enums
        // sets the stroke
        if (tileType.getValue() < 9) {
            // normal Tile
            setStroke(Paint.valueOf("CYAN"));
            setStrokeWidth(0.0);
        } else {
            // special Place
            setStroke(Paint.valueOf("GOLD"));
            setStrokeWidth(1.0);
        }
        setStrokeType(StrokeType.INSIDE);
    }

    /**
     * Set the rule for every generated special place to their rule.
     * @param tileType type for recognizing the special place.
     */
    private void setTokenTooltip(TileType tileType) {
        // TODO: Adjust to gameLogic enums
        Tooltip tokenTooltip = new Tooltip();
        switch (tileType) {
            case BARN -> {
                tokenTooltip.setText(resourceBundle.getString("tokenBarnRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case FARM -> {
                tokenTooltip.setText(resourceBundle.getString("tokenFarmRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case OASIS -> {
                tokenTooltip.setText(resourceBundle.getString("tokenOasisRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case TOWER -> {
                tokenTooltip.setText(resourceBundle.getString("tokenTowerRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case HARBOR -> {
                tokenTooltip.setText(resourceBundle.getString("tokenHarborRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case ORACLE -> {
                tokenTooltip.setText(resourceBundle.getString("tokenOracleRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case TAVERN -> {
                tokenTooltip.setText(resourceBundle.getString("tokenTavernRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case PADDOCK -> {
                tokenTooltip.setText(resourceBundle.getString("tokenPaddockRule"));
                Tooltip.install(this, tokenTooltip);
            }
        }

    }

    /**
     * Sets the MouseHandler of a hexagon based on its type.
     * @param tileType The type of the hexagon.
     */
    private void setMouseHandler(TileType tileType) {
        setOnMouseEntered( event -> {
            if (highlight) {
                setStroke(Paint.valueOf("DARKORCHID"));
            } else {
                setStroke(Paint.valueOf("RED"));
            }
            setStrokeWidth(2.0);
        });

        setOnMouseMoved(event -> {
            if (highlight) {
                setStroke(Paint.valueOf("DARKORCHID"));
            } else {
                setStroke(Paint.valueOf("RED"));
            }
            setStrokeWidth(2.0);
        });

        // TODO: Adjust to gameLogic enums (only Tiles have no Border and Gold Border for Tokens)
        if (tileType.getValue() < 9) {
            // placeable Tile
            setOnMouseExited(event -> {
                setStroke(Paint.valueOf("CYAN"));
                if (highlight) {
                    setStrokeWidth(2.0);
                } else {
                    setStrokeWidth(0.0);
                }
            });
        } else {
            // special Tile
            setOnMouseExited(event -> {
                setStroke(Paint.valueOf("GOLD"));
                setStrokeWidth(2.0);
            });
        }
    }

    /**
     * Activates the highlight of a Tile.
     */
    public void setHighlight() {
        highlight = true;
        setStroke(Paint.valueOf("CYAN"));
        setStrokeWidth(2.0);
    }

    /**
     * Removes the highlight from the tile.
     */
    public void removeHighlight() {
        highlight = false;
        setStrokeWidth(0.0);
    }

    /**
     * Gets the boolean value, if the tile is currently highlighted.
     * @return If the tile is highlighted.
     */
    public boolean isHighlighted() {
        return highlight;
    }

    /**
     * Gets the type of the hexagon.
     * @return The Type of the hexagon.
     */
    public TileType getTileType() {
        return tileType;
    }
}
