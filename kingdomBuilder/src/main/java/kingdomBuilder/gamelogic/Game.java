package kingdomBuilder.gamelogic;

import java.util.EnumSet;

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
     * Tiles that represent a hexagon in game board.
     */
    public enum TileType {
        // normal tiles
        GRAS,
        FLOWER,
        FORREST,
        CANYON,
        DESERT,
        WATER,
        MOUNTAIN,

        // special place
        CASTLE,

        // token tiles
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
     * Types of regular tiles.
     */
    public static EnumSet<TileType> regularTileTypes = EnumSet.range(TileType.GRAS, TileType.MOUNTAIN);

    /**
     * Types of tiles where the settlement can be placed without a token.
     */
    public static EnumSet<TileType> placeableTileTypes = EnumSet.range(TileType.GRAS, TileType.DESERT);

    /**
     * Types of tiles where a settlement can never be placed.
     */
    public static EnumSet<TileType> nonPlaceableTileTypes = EnumSet.range(TileType.MOUNTAIN, TileType.OASIS);

    /**
     * Types of token that can be acquired.
     */
    public static EnumSet<TileType> tokenType = EnumSet.range(TileType.ORACLE, TileType.OASIS);

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
