package kingdomBuilder.gamelogic;

/**
 * Win conditions for the game.
 */
public enum WinCondition {
    /**
     * Score for every settlement next to water.
     */
    FISHER,

    /**
     * Score for settlement next to a single mountain.
     */
    LORDS,

    /**
     * Score for every connection between two special places.
     */
    MINER,

    /**
     * Score for every settlement next to a special place.
     */
    ANCHORITE,

    /**
     * Score for every horizontal line with min. one settlement.
     */
    FARMER,

    /**
     * Score for every settlement on the horizontal line with the most settlements of your own.
     */
    MERCHANT,

    /**
     * Score for every separate group of settlements.
     */
    KNIGHT,

    /**
     * Score per quadrant for the player with the most settlements (second gets +6 score).
     */
    EXPLORER,

    /**
     * Score per settlement in the biggest group of settlements.
     */
    CITIZEN,

    /**
     * Score per quadrant for the player with the least settlements (second gets +6 score).
     */
    WORKER
}
