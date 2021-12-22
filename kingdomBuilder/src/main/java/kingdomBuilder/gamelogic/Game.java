package kingdomBuilder.gamelogic;

/**
 * Contains all the information of a game hosted on the server.
 */
public class Game {

    // Data from hosting a game.
    String gameName;
    String gameDescription;
    int playerLimit;
    int timeLimit;
    int turnLimit;
    QuadrantIDs quadrantIDs;

    // Additional data for a game.
    int hostID;
    int currentPlayerID;
    WinCondition winConditions[];
    Player players[];

    /**
     * Server takes quadrant IDs in this listed order.
     */
    public class QuadrantIDs {
        int leftUpper;
        int rightUpper;
        int lowerLeft;
        int lowerRight;
    }

    /**
     * Win conditions for the game.
     */
    public enum WinCondition {
        FISHER,
        LORDS,
        MINER,
        ANCHORITE,
        FARMER,
        MERCHANT,
        KNIGHT,
        EXPLORER,
        CITIZEN,
        WORKER
    }

    /**
     * Tiles that represent a hexagon in gameboard.
     */
    public enum TileType {
        // normal tiles
        GRAS,
        FLOWER,
        FOREST,
        CANYON,
        DESERT,
        WATER,
        MOUNTAIN,

        // token tiles
        CASTLE,
        SPECIAL_PLACE;
    }

    /**
     * Types of token that can be acquired.
     */
    public enum TokenType {
        ORACLE,
        FARM,
        TAVERN,
        TOWER,
        HARBOR,
        PADDOCK,
        BARN,
        OASIS;
    }

    /**
     * Colors for each player that are assign in order of joining the game.
     */
    public enum PlayerColor {
        RED,
        BLUE,
        BLACK,
        WHITE;
    }
}
