package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
     *
     * @param gameName            The name of the game.
     * @param gameDescription     The description of the game.
     * @param playerLimit         The maximum amount of players that can play in the game.
     * @param timeLimit           The maximum amount of time a player can spend on each turn.
     * @param turnLimit           The maximum amount of turns the game can run for.
     * @param quadrantIDs         The quadrants the game board is assembled from.
     * @param hostID              The ID of the client who created the game.
     * @param startingPlayerID    The ID of the player who takes the first turn.
     * @param winConditions       An array of the win conditions of the game.
     * @param players             An array of the players playing in the game.
     * @param startingTokenCount  The amount of tokens each special place should contain at the start of the game.
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
            for (int x = 0; x < map.getMapWidth(); x++) {
                if (map.occupiedBy(x, y) == player) {
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
     *
     * @param x      The horizontal position of the settlement on the map.
     * @param y      The vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     * @return
     */
    public boolean canPlaceSettlement(Player player, int x, int y) {
        //TODO: implement settlement placement WITHOUT a terrain
        return isPlayersTurn(player)
                && player.hasRemainingSettlements()
                && !map.isOccupied(x, y);
    }

    /**
     * Places a settlement as a basic turn and throws if the move isn't valid.
     *
     * @param x      The horizontal position of the settlement on the map.
     * @param y      The vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     * @return
     */
    public void placeSettlement(Player player, int x, int y) {
        checkIsPlayersTurn(player);
        checkHasRemainingSettlements(player);

        map.placeSettlement(player, x, y);
        player.remainingSettlements--;

        TileType token = map.specialPlaceInSurrounding(x, y);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.takeToken(x, y);
        }
    }

    /**
     * Checks if a settlement can be moved to a new position.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     *
     * @param fromX  The old horizontal position of the settlement on the map.
     * @param fromY  The old vertical position of the settlement on the map.
     * @param toX    The new horizontal position of the settlement on the map.
     * @param toY    The new vertical position of the settlement on the map.
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
     *
     * @param fromX  The old horizontal position of the settlement on the map.
     * @param fromY  The old vertical position of the settlement on the map.
     * @param toX    The new horizontal position of the settlement on the map.
     * @param toY    The new vertical position of the settlement on the map.
     * @param player The player whose turn it is and who owns the settlement.
     */
    public void moveSettlement(Player player, int fromX, int fromY, int toX, int toY) {

        checkIsPlayersTurn(player);

        map.moveSettlement(fromX, fromY, toX, toY);

        TileType token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.takeToken(toX, toY);
        }
    }

    /**
     * Moves a settlement to a new position and throws if the move isn't valid.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     *
     * @param player  The player whose turn it is and who owns the settlement.
     * @param terrain The terrain it is getting placed.
     * @param fromX   The old horizontal position of the settlement on the map.
     * @param fromY   The old vertical position of the settlement on the map.
     * @param toX     The new horizontal position of the settlement on the map.
     * @param toY     The new vertical position of the settlement on the map.
     */
    public void moveSettlement(Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {

        checkIsPlayersTurn(player);

        if (map.occupiedBy(fromX, fromY) != player || !canPlaceSettlementInTerrain(player, terrain, toX, toY))
            return;

        // Take token from player if settlement was last one on special place
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {

            TileType token = map.specialPlaceInSurrounding(fromX, fromY);

            if (player.playerHasTokenLeft(token))
                player.removeToken(token);
        }

        map.moveSettlement(fromX, fromY, toX, toY);


        TileType token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.takeToken(toX, toY);
        }
    }


    /**
     * Gives player a token and removes it from the special place.
     *
     * @param player The player the token is given to.
     * @param x      The x coordinate of the tile.
     * @param y      The y coordinate of the tile.
     */
    public void addToken(Player player, int x, int y) {
        //TODO: don't add token from the same place twice

        if (map.tileHasTokenLeft(x, y)) {
            player.addToken(map.getTileType(x, y));
            map.takeToken(x, y);
        }
    }

    /**
     * use a token that places a settlement.
     *
     * @param tokenType that is used.
     * @param player    player that uses it.
     */
    public void useToken(TileType tokenType, Player player, int x, int y) {
        // TODO: only keep this if token function are getting changed to private
        if (!player.playerHasTokenLeft(tokenType))
            return;

        switch (tokenType) {
            case ORACLE:
                useTokenOracle(player, x, y);
                break;
            case FARM:
                useTokenFarm(player, x, y);
                break;
            case TAVERN:
                useTokenTavern(player, x, y);
                break;
            case TOWER:
                useTokenTower(player, x, y);
                break;
            case OASIS:
                useTokenOasis(player, x, y);
                break;
            default:
                return;
        }

    }

    /**
     * use a token that moves a settlement.
     *
     * @param tokenType
     * @param player
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    public void useToken(TileType tokenType, Player player, int fromX, int fromY, int toX, int toY) {
        // TODO: only keep this if token function are getting changed to private
        if (!player.playerHasTokenLeft(tokenType))
            return;

        switch (tokenType) {
            case HARBOR:
                useTokenHarbor(player, fromX, fromY, toX, toY);
                break;
            case PADDOCK:
                useTokenPaddock(player, fromX, fromY, toX, toY);
                break;
            case BARN:
                useTokenBarn(player, fromX, fromY, toX, toY);
                break;
            default:
                return;
        }

    }


    /**
     * Use the oracle token. The player is allowed to place an extra settlement on a tile that has the same type as
     * the players' terrain card.
     *
     * @param player That is using the token.
     * @param x      The x position of the settlement to place.
     * @param y      The y position of the settlement to place.
     */
    public void useTokenOracle(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, player.terrainCard, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.ORACLE);
        }
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player That is using the token.
     * @param x      The x position of the settlement to place.
     * @param y      The y position of the settlement to place.
     */
    public void useTokenFarm(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, TileType.GRAS, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.FARM);
        }
    }

    // TODO: implement, parameters should match the protocol message format
    public void useTokenTavern(Player player, int x, int y) {
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player That is using the token.
     * @param x      The x position of the settlement to place.
     * @param y      The y position of the settlement to place.
     */
    public void useTokenTower(Player player, int x, int y) {
        if (!map.isTileAtBorder(x, y))
            return;

        if (canPlaceSettlement(player, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.TOWER);
        }
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player That is using the token.
     * @param x      The x position of the settlement to place.
     * @param y      The y position of the settlement to place.
     */
    public void useTokenOasis(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, TileType.DESERT, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.OASIS);
        }
    }

    // TODO: implement, parameters should match the protocol message format
    public void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
    }

    // TODO: implement, parameters should match the protocol message format
    public void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
    }

    // TODO: implement, parameters should match the protocol message format
    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
        player.removeToken(TileType.BARN);
        moveSettlement(player, player.terrainCard, fromX, fromY, toX, toY);
    }


    /**
     * check if a settlement is placeable.
     *
     * @param player
     * @param terrain
     * @param x
     * @param y
     * @return
     */
    public boolean canPlaceSettlementInTerrain(Player player, TileType terrain, int x, int y) {

        if (!isPlayersTurn(player) || !player.hasRemainingSettlements())
            return false;

        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("parameter is not a terrain!");

        // terrain must match the terrain of the tile player wants to place a settlement
        if (!map.isTilePlaceable(x, y) && terrain != map.getTileType(x, y))
            return false;

        // player has to place settlement next to another settlement
        if (map.settlementOfPlayerOnSurroundingTiles(player, x, y))
            return true;

        // player can place anywhere on tile
        if (playerCanPlaceAnywhereOnTerrain(player, terrain, x, y))
            return true;

        return false;
    }


    /**
     * Checks if player can place anywhere on a terrain.
     *
     * @param player
     * @param terrain
     * @param x
     * @param y
     * @return
     */
    public boolean playerCanPlaceAnywhereOnTerrain(Player player, TileType terrain, int x, int y) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Position> allPlaceableTiles = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);

        if (allPlaceableTiles.isEmpty())
            return true;

        return false;
    }

    /**
     * Gets all possible positions to place a settlement for a player on a given terrain.
     *
     * @param player
     * @param terrain
     * @return
     */
    public Set<Position> allPossibleSettlementPlacementsNextToOtherSettlement(Player player, TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Position> allowedTiles = new HashSet<>();
        Set<Position> freeTiles = map.freeTilesOnTerrain(terrain);
        Iterator<Position> freeTilesIterator = freeTiles.iterator();
        Position current;

        while (freeTilesIterator.hasNext()) {
            current = freeTilesIterator.next();

            if (map.settlementOfPlayerOnSurroundingTiles(player, current.x, current.y)) {
                allowedTiles.add(current);
            }
        }
        return allowedTiles;
    }


    // TODO: implement basic functions of MAP again in Game but with rules (e.g. Check if player is next to token)

}
