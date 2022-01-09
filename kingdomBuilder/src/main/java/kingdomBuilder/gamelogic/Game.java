package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.EnumSet;

/**
 * Contains all the information of a game hosted on the server.
 */
public class Game {

    // TODO: maybe isolate this in a separate class for storage after ?games request
    // Data from hosting/joining a game.
    /**
     * The name of the game.
     */
    private final String gameName;

    /**
     * The description of the game.
     */
    private final String gameDescription;

    // TODO: copy paste descriptions from the constructor :)
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
    public static final int DEFAULT_STARTING_TOKEN_COUNT = 2;
    public static final int DEFAULT_STARTING_SETTLEMENTS = 40;

    // Internal data of the map
    private final Map map;

    /**
     * Constructs a new game object which is ready for the first move.
     * @param gameName The name of the game.
     * @param gameDescription The description of the game.
     * @param playerLimit The maximum amount of players that can play in the game.
     * @param timeLimit The maximum amount of time a player can spend on each turn.
     * @param turnLimit The maximum amount of turns the game can run for.
     * @param quadrantIDs The quadrants the game board is assembled from.
     * @param hostID The ID of the client who created the game.
     * @param startingPlayerID The ID of the player who takes the first turn.
     * @param winConditions An array of the win conditions of the game.
     * @param players An array of the players playing in the game.
     * @param startingTokenCount The amount of tokens each special place should contain at the start of the game.
     * @param startingSettlements The total amount of settlements a player has to place to end the game.
     */
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

    // TODO: maybe return an iterator instead
    public int[] placeableTilesWithLandscape(TileType landscape, Player player) {
        if (!regularTileTypes.contains(landscape))
            throw new InvalidParameterException("Type of tile is not a landscape");


        //Tile[] tilesWithSettlementsOfPlayer = new Tile[startingSettlements - player.remainingSettlements];
        int counter = 0;

        for (int y = 0; y < map.getMapWidth(); y++) {
            for (int x = 0; x < map.getMapWidth(); x++){
                if(map.occupiedBy(x, y) == player){
                    //tilesWithSettlementsOfPlayer[counter] =
                }
            }
        }

        return null;
    }

    private void checkIsPlayersTurn(Player player) {
        if (!isPlayersTurn(player))
            throw new RuntimeException("It's not the player's turn!");
    }

    private void checkHasRemainingSettlements(Player player) {
        if (!player.hasRemainingSettlements())
            throw new RuntimeException("Player has no settlements left!");
    }

    // TODO: change parameter order so everything starts with Player probably (also in Player, Map and Tile classes)

    /**
     * Checks if a settlement can be placed.
     * @param x The horizontal position of the settlement on the map.
     * @param y The vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     * @return
     */
    public boolean canPlaceSettlement(int x, int y, Player player) {
        return isPlayersTurn(player)
                && player.hasRemainingSettlements()
                && !map.isOccupied(x, y);
    }

    /**
     * Places a settlement as a basic turn and throws if the move isn't valid.
     * @param x The horizontal position of the settlement on the map.
     * @param y The vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     * @return
     */
    public void placeSettlement(int x, int y, Player player) {
        checkIsPlayersTurn(player);
        checkHasRemainingSettlements(player);

        map.placeSettlement(x, y, player);
        player.remainingSettlements--;
        // TODO: check if new position neighbours special place, if so give token to player
    }

    /**
     * Checks if a settlement can be moved to a new position.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     * @param fromX The old horizontal position of the settlement on the map.
     * @param fromY The old vertical position of the settlement on the map.
     * @param toX The new horizontal position of the settlement on the map.
     * @param toY The new vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     */
    public boolean canMoveSettlement(int fromX, int fromY, int toX, int toY, Player player) {
        return isPlayersTurn(player)
                && (map.occupiedBy(fromX, fromY) == player)
                && !map.isOccupied(toX, toY)
                && map.isTilePlaceable(toX, toY);
    }

    /**
     * Moves a settlement to a new position and throws if the move isn't valid.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     * @param fromX The old horizontal position of the settlement on the map.
     * @param fromY The old vertical position of the settlement on the map.
     * @param toX The new horizontal position of the settlement on the map.
     * @param toY The new vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     */
    public void moveSettlement(int fromX, int fromY, int toX, int toY, Player player) {
        checkIsPlayersTurn(player);

        map.moveSettlement(fromX, fromY, toX, toY);
        // TODO: check if previous position neighboured special place, if so remove token from player
        // TODO: check if new position neighbours special place, if so give token to player
    }

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
