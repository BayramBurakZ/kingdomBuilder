package kingdomBuilder.gamelogic;

/**
 * Class to describe a position.
 */
public class Position {

    /**
     * The x-coordinate.
     */
    int x;

    /**
     * The y-coordinate.
     */
    int y;

    /**
     * Constructs a new Position object.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Position object.
     * @param pos the position.
     */
    public Position(Position pos) {
        this.x = pos.x;
        this.y = pos.y;
    }
}
