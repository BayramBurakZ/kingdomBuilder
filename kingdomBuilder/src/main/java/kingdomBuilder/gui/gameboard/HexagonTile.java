package kingdomBuilder.gui.gameboard;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;

/**
 * Class that is used to display the hexagon tiles in the UI.
 */
public class HexagonTile extends Polygon {
    /**
     * Represents the angle in degree to turn the corners around the center;
     */
    private final int TURN_ANGLE_DEGREE = 60;

    /**
     * Represents the angle in radian to turn the corners around the center;
     */
    private final double TURN_ANGLE_RADIAN = Math.toRadians(TURN_ANGLE_DEGREE);

    /**
     * Represents the number of corners of a hexagon.
     */
    private final int NUMBER_OF_CORNERS = 6;

    /**
     * Represents the six corners of a hexagon.
     */
    private static ArrayList<Point> corners;

    /**
     * Creates a new Hexagon Tile at the given position.
     * @param xPos The x-coordinate of the upper-left corner position.
     * @param yPos The y-coordinate of the upper-left corner position.
     */
    public HexagonTile(double xPos, double yPos) {
        // only calculate the corners once and not for every hexagon
        if (corners == null) {
            //ToDo: extract the hard coded radius
            corners = calculateCorners(40);
        }

        // calculate all corners of a hexagon
        for (int i = 0; i < NUMBER_OF_CORNERS; i++) {
            getPoints().add(xPos + corners.get(i).getX());
            getPoints().add(yPos + corners.get(i).getY());
        }

        setStroke(Paint.valueOf("BLACK"));
        setStrokeType(StrokeType.INSIDE);
        setFill(Paint.valueOf("WHITE"));

        //ToDo: StrokeColor depends on TileType (eg. normal tile black and tokens gold)
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

        setOnMouseExited(new EventHandler<MouseEvent>() {
            /**
             * A MouseEvent handler to end the highlight of the hexagon.
             * @param event A MouseEvent
             */
            @Override
            public void handle(MouseEvent event) {
                setStroke(Paint.valueOf("BLACK"));
                setStrokeWidth(1.0);
            }
        });
    }

    /**
     * Sets the texture with the given Image.
     * @param texture The Image for the texture.
     */
    public void setTexture(Image texture) {
        setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
    }

    /**
     * Class the represents a point.
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

        return corners;
    }
}
