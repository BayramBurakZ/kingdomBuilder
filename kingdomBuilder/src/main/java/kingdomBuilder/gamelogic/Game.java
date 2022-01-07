package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.EnumSet;

/**
 * Contains all the information of a game hosted on the server.
 */
public class Game {

    // TODO: maybe isolate this in a separate class for storage after ?games request
    // Data from hosting/joining a game.
    private final String gameName;
    private final String gameDescription;
    private final int playerLimit;
    private final int timeLimit;
    private final int turnLimit;
    private final QuadrantIDs quadrantIDs;

    // Additional data for a game.
    private final int hostID;
    private int currentPlayerID;
    private final WinCondition[] winConditions;
    private final Player[] players;
    private final int startingSettlements;

    // constants
    public static final int SETTLEMENTS_PER_TURN = 3;
    public static final int DEFAULT_STARTING_SETTLEMENTS = 40;

    // Internal data of the map
    private final Map map;

    public Game(String gameName,
                String gameDescription,
                int playerLimit,
                int timeLimit,
                int turnLimit,
                QuadrantIDs quadrantIDs,
                int hostID,
                int startingPlayerID,
                WinCondition[] winConditions,
                Player[] players,
                int startingTokenCount,
                int startingSettlements
    ) {
        if (players.length > playerLimit)
            throw new RuntimeException("The amount of players surpasses the player limit!");

        if (winConditions.length <= 0)
            throw new RuntimeException("No win conditions have been defined!");

        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.playerLimit = playerLimit;
        this.timeLimit = timeLimit;
        this.turnLimit = turnLimit;
        this.quadrantIDs = quadrantIDs;
        this.hostID = hostID;
        this.currentPlayerID = startingPlayerID;
        this.winConditions = winConditions;
        // TODO: the players array should be created by the Game itself
        //       the constructor should only receive the player information that was relevant before game start
        this.players = players;
        this.startingSettlements = startingSettlements;
        this.map = new Map(startingTokenCount);
    }

    // TODO: maybe not a class for this idk
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
        OASIS
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
        WHITE
    }

    public boolean isPlayersTurn(Player player) {
        return currentPlayerID == player.ID;
    }

    private void checkIfPlayersTurn(Player player) {
        if (!isPlayersTurn(player))
            throw new RuntimeException("It's not the player's turn!");
    }

    public void placeSettlementBasicTurn(int x, int y, Player player) {
        checkIfPlayersTurn(player);

        map.placeSettlement(x, y, player);
    }

    // TODO: maybe return an iterator instead
    public int[] placeableTilesWithLandscape(TileType landscape, Player player) {
        if (!regularTileTypes.contains(landscape))
            throw new InvalidParameterException("Type of tile is not a landscape");


        //Tile[] tilesWithSettlementsOfPlayer = new Tile[startingSettlements - player.remainingSettlements];
        int counter = 0;

        for (int y = 0; y < map.getMapWidth(); y++) {
            for (int x = 0; x < map.getMapWidth(); x++){
                if(map.tileIsOccupiedByPlayer(x, y, player)){
                    //tilesWithSettlementsOfPlayer[counter] =
                }

            }
        }

        return null;
    }

    // TODO: change parameter order so everything starts with Player probably (also in Player, Map and Tile classes)

    // TODO: implement, see Map function by the same name
    //placeSettlement(int x, int y, Player player)

    // TODO: implement, see Map function by the same name
    //moveSettlement(int x, int y, Player player)

    // TODO: implement, see Player function by the same name
    //addToken(TileType tokenType, Player player, ...)

    // TODO: implement, parameters should match the protocol message format
    //useTokenOracle(Player player, int x, int y)

    // TODO: implement, parameters should match the protocol message format
    //useTokenFarm(Player player, int x, int y)

    // TODO: implement, parameters should match the protocol message format
    //useTokenTavern(Player player, int x, int y)

    // TODO: implement, parameters should match the protocol message format
    //useTokenTower(Player player, int x, int y)

    // TODO: implement, parameters should match the protocol message format
    //useTokenOasis(Player player, int x, int y)

    // TODO: implement, parameters should match the protocol message format
    //useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY)

    // TODO: implement, parameters should match the protocol message format
    //useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY)

    // TODO: implement, parameters should match the protocol message format
    //useTokenBarn(Player player, int fromX, int fromY, int toX, int toY)
}
