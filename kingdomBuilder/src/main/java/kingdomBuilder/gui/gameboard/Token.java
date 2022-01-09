package kingdomBuilder.gui.gameboard;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import kingdomBuilder.gui.util.Point;
import kingdomBuilder.model.TileType;

import java.util.ArrayList;

/**
 * Class to create Tokens for the inventory.
 */
public class Token extends Polygon {
    /**
     * Represents the six corners of a hexagon.
     */
    private static ArrayList<Point> vertices;

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
     * Calculates the corners of a hexagon with the given radius.
     * @param radius The radius from the center of a hexagon to one of its corners.
     * @return A list with six points of a hexagon.
     */
    private static ArrayList<Point> calculateCorners(int radius) {
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

    public Token(TileType tileType) {
        vertices = calculateCorners(40);

        // calculate all corners of a hexagon
        for (int i = 0; i < NUMBER_OF_CORNERS; i++) {
            getPoints().add((double) vertices.get(i).getX());
            getPoints().add((double) vertices.get(i).getY());
        }

        Image texture = textureLoader.getTexture(tileType);
        setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
    }
}
