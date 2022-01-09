package kingdomBuilder.gui.util;

/**
 * Class that represents a point.
 */
public class Point {

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