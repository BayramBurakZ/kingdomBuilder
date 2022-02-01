package kingdomBuilder.gamelogic;

/**
 * This describes a turn made by the client.
 */
public final class ServerTurn extends Turn {

    /**
     * Represents the type of turn the server sends.
     */
    public final TurnType type;

    /**
     * Creates a new ServerTurn.
     * @param type the type of turn the server sends.
     * @param x the x-coordinate for placing/removing a settlement.
     * @param y the y-coordinate for placing/removing a settlement.
     * @param toX the x-coordinate for moving a settlement.
     * @param toY the y-coordinate for moving a settlement.
     */
    public ServerTurn(
            int playerId,
            TurnType type,
            int x, int y,
            int toX, int toY
    ) {
        super(playerId, x, y, toX, toY);
        this.type = type;
    }

    /**
     * Represents the type for the turn.
     */
    public enum TurnType {
        /**
         * Represents the type to place a settlement.
         */
        PLACE,
        /**
         * Represents the type to remove a settlement.
         */
        REMOVE,
        /**
         * Represents the type to move a settlement.
         */
        MOVE
    }
}
