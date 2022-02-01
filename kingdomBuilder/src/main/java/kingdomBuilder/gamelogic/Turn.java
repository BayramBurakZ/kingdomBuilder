package kingdomBuilder.gamelogic;

/**
 * Abstract class to define a turn.
 */
public abstract class Turn {

    /**
     * Represents the client that did the turn.
     */
    public final int clientId;

    /**
     * Represents the x-coordinate for placing/removing a settlement.
     */
    public final int x;

    /**
     * Represents the y-coordinate for placing/removing a settlement.
     */
    public final int y;

    /**
     * Represents the x-coordinate to move a settlement to.
     */
    public final int toX;

    /**
     * Represents the x-coordinate to move a settlement to.
     */
    public final int toY;

    /**
     * Creates a new turn.
     * @param clientId the id of the client
     * @param x the x-coordinate to place a settlement or remove it.
     * @param y the y-coordinate to place a settlement or remove it.
     * @param toX the x-coordinate to move the settlement to.
     * @param toY the y-coordinate to move the settlement to.
     */
    protected Turn(int clientId, int x, int y, int toX, int toY) {
        this.clientId = clientId;
        this.x = x;
        this.y = y;
        this.toX = toX;
        this.toY = toY;
    }
}
