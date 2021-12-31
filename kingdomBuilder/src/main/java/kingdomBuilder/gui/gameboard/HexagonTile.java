package kingdomBuilder.gui.gameboard;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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

        if (resourceBundle == null) {
            resourceBundle = resource;
        }

        // calculate all corners of a hexagon
        for (int i = 0; i < NUMBER_OF_CORNERS; i++) {
            getPoints().add(xPos + corners.get(i).getX());
            getPoints().add(yPos + corners.get(i).getY());
        }

        setTexture(textureLoader.getTexture(tileType));
        setHexagonStroke(tileType);
        setMouseHandler(tileType);
    }

    /**
     * Class that represents a point.
     */
    private class Point {

        /**
         * Represents the x-coordinate of the point.
         */
        private int x;
        /**
         * Represents the y-coordinate of the point.
         */
        private int y;
        /**
         * Constructs a new point with the given parameters.
         * @param x The x-coordinate of the point.
         * @param y The y-coordinate of the point.
         */
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Gets the x-coordinate of the point.
         * @return The x-coordinate.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the y-coordinate of the point.
         * @return The y-coordinate.
         */
        public int getY() {
            return y;
        }

        /**
         * Translates the x-coordinate of a point with the given distance.
         * @param x The value that the point is moved on the x-axis.
         */
        public void translateX(int x) {
            this.x += x;
        }

        /**
         * Translates the y-coordinate of a point with the given distance.
         * @param y The value that the point is moved on the y-axis.
         */
        public void translateY(int y) {
            this.y += y;
        }
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
            if (e.x < minX) {
                minX = e.x;
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
            setStrokeWidth(0.0);
        } else {
            // special Place
            setStroke(Paint.valueOf("GOLD"));
            setStrokeWidth(1.0);
        }

        setStrokeType(StrokeType.INSIDE);
    }

    /**
     * Sets the MouseHandler of a hexagon based on its type.
     * @param tileType The type of the hexagon.
     */
    private void setMouseHandler(TileType tileType) {
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            /**
             * A MouseEvent handler to highlight the hexagon.
             * @param event A MouseEvent
             */
            @Override
            public void handle(MouseEvent event) {
                setStroke(Paint.valueOf("RED"));
                setStrokeWidth(2.0);
            }
        });

        // TODO: Adjust to gameLogic enums (only Tiles have no Border and Gold Border for Tokens)
        if (tileType.getValue() < 9) {
            setOnMouseExited(new EventHandler<MouseEvent>() {
                /**
                 * A MouseEvent handler to end the highlight of the hexagon.
                 * @param event A MouseEvent
                 */
                @Override
                public void handle(MouseEvent event) {
                    setStrokeWidth(0.0);
                }
            });
        } else {
            setOnMouseExited(new EventHandler<MouseEvent>() {
                /**
                 * A MouseEvent handler to end the highlight of the hexagon.
                 * @param event A MouseEvent
                 */
                @Override
                public void handle(MouseEvent event) {
                    setStroke(Paint.valueOf("GOLD"));
                    setStrokeWidth(1.0);
                }
            });
        }
    }
}
