package kingdomBuilder.gamelogic;

/**
 * This describes a turn made by the mainClient.
 */
public final class ClientTurn extends Turn {

    /**
     * Represents the type of turn the mainClient did.
     */
    public final TurnType type;

    /**
     * Creates a new mainClient turn.
     * @param playerId the id of the player who makes the turn.
     * @param type the type of turn which the mainClient did.
     * @param x the x-coordinate to place/remove the settlement.
     * @param y the y-coordinate to place/remove the settlement.
     * @param toX the x-coordinate for moving a settlement.
     * @param toY the y-coordinate for moving a settlement.
     */
    public ClientTurn(
            int playerId,
            TurnType type,
            int x, int y,
            int toX, int toY
    ) {
        super(playerId, x, y, toX, toY);
        this.type = type;
    }

    /**
     * Represents the type of turn the mainClient can perform.
     */
    public enum TurnType {
        /**
         * Represents the basic turn to place one settlement.
         */
        PLACE,

        /**
         * Represents the use of an oracle token.
         */
        ORACLE,
        /**
         * Represents the use of a farm token.
         */
        FARM,
        /**
         * Represents the use of a tavern token.
         */
        TAVERN,
        /**
         * Represents the use of a tower token.
         */
        TOWER,
        /**
         * Represents the use of a harbor token.
         */
        HARBOR,
        /**
         * Represents the use of a paddock token.
         */
        PADDOCK,
        /**
         * Represents the use of a barn token.
         */
        BARN,
        /**
         * Represents the use of an oasis token.
         */
        OASIS
    }
}
