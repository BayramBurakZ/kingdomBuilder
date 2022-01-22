package kingdomBuilder.gui.util;

import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * This class is used to calculate the corners of a hexagon.
 */
public class HexagonCalculator {
    /**
    * Represents the number of corners of a hexagon.
    */
    public static final int NUMBER_OF_CORNERS = 6;

    /**
     * Represents the angle in degree to turn the corners around the center;
     */
    private static final int TURN_ANGLE_DEGREE = 60;

    /**
     * Represents the angle in radian to turn the corners around the center;
     */
    private static final double TURN_ANGLE_RADIAN = Math.toRadians(TURN_ANGLE_DEGREE);

    /**
     * Calculates the corners of a hexagon with the given radius.
     *
     * @param radius the radius from the center of a hexagon to one of its corners.
     * @return A list with six points of a hexagon.
     */
    public static ArrayList<Point2D> calculateCorners(int radius) {
        ArrayList<Point2D> corners = new ArrayList<>();

        int x = 0, my = 0;
        int y = my + radius;

        corners.add(new Point2D(x, y));

        // Rotation matrix to rotate the corners 60 degree around the center
        for (int i = 0; i < NUMBER_OF_CORNERS - 1; i++) {
            int movedX = (int) Math.round((x * Math.cos(TURN_ANGLE_RADIAN)) - (y * Math.sin(TURN_ANGLE_RADIAN)));
            int movedY = (int) Math.round((x * Math.sin(TURN_ANGLE_RADIAN)) + (y * Math.cos(TURN_ANGLE_RADIAN)));

            corners.add(new Point2D(movedX, movedY));
            x = movedX;
            y = movedY;
        }

        // search the distance from the center to the far left side
        int minX = Integer.MAX_VALUE;
        for (Point2D e : corners) {
            if (e.getX() < minX) {
                minX = (int) e.getX();
            }
        }


        //translate the hexagon corners to positive values
        for (int i = 0; i < corners.size(); i++) {
            corners.set(i, corners.get(i).add(-minX, radius));
        }
        return corners;
    }
}
