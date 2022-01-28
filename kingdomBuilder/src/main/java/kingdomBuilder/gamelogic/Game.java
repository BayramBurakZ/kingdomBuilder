package kingdomBuilder.gamelogic;

import kingdomBuilder.network.protocol.MyGameReply;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Contains all the information of a game hosted on the server.
 */
public class Game {

    // constants
    /**
     * Represents how many settlements can be placed with the basic turn.
     */
    public static final int SETTLEMENTS_PER_TURN = 3;

    /**
     * Represents the amount of settlements with which the player starts.
     */
    public static final int DEFAULT_STARTING_SETTLEMENTS = 40;

    // TODO: maybe isolate this in a separate class for storage after ?games request
    // Data from hosting/joining a game.
    /**
     * The name of the game.
     */
    private String gameName;

    /**
     * The description of the game.
     */
    private String gameDescription;

    /**
     * The maximum amount of players that can play in the game.
     */
    private int playerLimit;

    /**
     * The maximum amount of time a player can spend on each turn.
     */
    private int timeLimit;

    /**
     * The maximum amount of turns the game can run for.
     */
    private int turnLimit;

    // Additional data for a game.
    /**
     * The ID of the client who created the game.
     */
    private int hostID;

    /**
     * An array of the win conditions of the game.
     */
    private WinCondition[] winConditions;

    /**
     * The current terrain card of a players turn.
     */
    private TileType terrainCard;

    /**
     * An array of the players playing in the game.
     */
    private Player[] players;

    /**
     * A map of the players playing in the game.
     */
    public final HashMap<Integer, Player> playersMap = new HashMap<>();

    /**
     * The player for current turn.
     */
    private Player currentPlayer;

    /**
     * The total amount of settlements a player has to place to end the game.
     */
    private int startingSettlements;

    /**
     * The network message infos of the game.
     */
    private MyGameReply myGameReply;

    /**
     * Represents the state if the game has started.
     */
    private boolean gameRunning = false;

    /**
     * Internal data of the map
     */
    private Map map;

    /**
     * Constructs a new Game object.
     */
    public Game() {

    }

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

    /**
     * Tiles that represent a hexagon in game board.
     */
    public enum TileType {
        /**
         * Represents the grass type. It is a normal type.
         * Is part of following enum sets: regularTileTypes.
         */
        GRAS,
        /**
         * Represents the flower type. It is a normal type.
         * Is part of following enum sets: regularTileTypes, placeableTileTypes.
         */
        FLOWER,
        /**
         * Represents the forest type. It is a normal type.
         * Is part of following enum sets: regularTileTypes, placeableTileTypes.
         */
        FORREST,
        /**
         * Represents the canyon type. It is a normal type.
         * Is part of following enum sets: regularTileTypes, placeableTileTypes.
         */
        CANYON,
        /**
         * Represents the desert type. It is a normal type.
         * Is part of following enum sets: regularTileTypes, placeableTileTypes.
         */
        DESERT,
        /**
         * Represents the water type. It is a normal type.
         * Is part of following enum sets: regularTileTypes, placeableTileTypes.
         */
        WATER,
        /**
         * Represents the mountain type. It is a normal type.
         * Is part of following enum sets: regularTileTypes.
         */
        MOUNTAIN,
        /**
         * Represents the castle type. It is a normal type.
         * Is part of following enum sets: nonPlaceableTileTypes.
         */
        CASTLE,
        /**
         * Represents the oracle type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes.
         */
        ORACLE,
        /**
         * Represents the farm type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        FARM,
        /**
         * Represents the tavern type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        TAVERN,
        /**
         * Represents the tower type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        TOWER,
        /**
         * Represents the harbor type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        HARBOR,
        /**
         * Represents the paddock type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        PADDOCK,
        /**
         * Represents the barn type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
        BARN,
        /**
         * Represents the oasis type. It is a token type.
         * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
         */
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
        /**
         * Represents the RED-Color for the first player that joined.
         */
        RED,
        /**
         * Represents the BLUE-Color for the second player that joined.
         */
        BLUE,
        /**
         * Represents the BLACK-Color for the third player that joined.
         */
        BLACK,
        /**
         * Represents the WHITE-Color for the fourth player that joined.
         */
        WHITE
    }

    /**
     * Show all possible placements at the start of the turn.
     *
     * @return all possible tiles at the start of the game.
     */
    public Set<Tile> startTurn() {
        return updatePreviewWithTerrain(currentPlayer, currentPlayer.terrainCard);
    }

    /**
     * Checks if it's the given player's turn.
     *
     * @param player the player to check if it's their turn.
     * @return Whether it's the given player's turn.
     */
    public boolean isPlayersTurn(Player player) {
        return currentPlayer == player;
    }

    /**
     * Throws if it's not the given player's turn.
     *
     * @param player the player to check if it's their turn.
     */
    private void checkIsPlayersTurn(Player player) {
        if (!isPlayersTurn(player))
            throw new RuntimeException("It's not the player's turn!");
    }

    /**
     * Checks if the given player can still place settlements this turn.
     *
     * @param player the player to check if they can still place settlements this turn.
     */
    private void checkHasRemainingSettlements(Player player) {
        if (!player.hasRemainingSettlements())
            throw new RuntimeException("Player has no settlements left!");
    }

    /**
     * Checks if a settlement can be placed.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     * @return true if the a settlement can be placed.
     */
    private boolean canPlaceSettlement(Player player, int x, int y) {

        boolean hasSurroundingSettlement = false;

        for (var it = map.surroundingTilesIterator(x, y); it.hasNext(); ) {
            Tile pos = (Tile) it.next();
            if (map.at(pos.x, pos.y).occupiedBy == player) {
                // TODO: should also be true if no settlement has been placed at all
                hasSurroundingSettlement = true;
                break;
            }
        }

        boolean noFreeTilesNextToSettlements =
                (allPossibleSettlementPlacementsNextToOtherSettlement(player, player.terrainCard)).isEmpty();

        return isPlayersTurn(player)
                && player.hasRemainingSettlements()
                && !map.at(x, y).isOccupied()
                //&& map.at(x, y).isTilePlaceable()
                && (map.at(x, y).tileType == player.terrainCard && (hasSurroundingSettlement || !noFreeTilesNextToSettlements)
                    || map.getEntireTerrain(player.terrainCard).isEmpty());
    }

    /**
     * Checks if the player can place a settlement on a tile with the type of water.
     *
     * @param player the player.
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @return true if the player can build on that tile.
     */
    private boolean canPlaceSettlementOnWater(Player player, int x, int y) {

        if(map.at(x,y).tileType != TileType.WATER)
            return false;

        boolean hasSurroundingSettlement = false;

        for (var it = map.surroundingTilesIterator(x, y); it.hasNext(); ) {
            Tile pos = (Tile) it.next();
            if (map.at(pos.x, pos.y).occupiedBy == player) {
                hasSurroundingSettlement = true;
                break;
            }
        }

        boolean noFreeTilesNextToSettlements =
                (allPossibleSettlementPlacementsNextToOtherSettlement(player, player.terrainCard)).isEmpty();

        return isPlayersTurn(player)
                && player.hasRemainingSettlements()
                && !map.at(x, y).isOccupied()
                //&& map.at(x, y).isTilePlaceable()
                && map.at(x, y).tileType == player.terrainCard
                && (hasSurroundingSettlement || !noFreeTilesNextToSettlements);
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    public void placeSettlement(Player player, int x, int y) {
        if(!canPlaceSettlement(player, x, y))
            return;

        checkIsPlayersTurn(player);
        checkHasRemainingSettlements(player);

        map.at(x, y).placeSettlement(player);
        player.remainingSettlements--;

        TileType token = map.specialPlaceInSurrounding(x, y);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(x, y).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Removes a settlement from a player.
     *
     * @param player
     * @param x
     * @param y
     */
    public void removeSettlement(Player player, int x, int y){

    }

    /**
     * Moves a settlement to a new position and throws if the move is not valid.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     *
     * @param player  the player whose turn it is and who owns the settlement.
     * @param terrain the terrain it is getting placed.
     * @param fromX   the old horizontal position of the settlement on the map.
     * @param fromY   the old vertical position of the settlement on the map.
     * @param toX     the new horizontal position of the settlement on the map.
     * @param toY     the new vertical position of the settlement on the map.
     */
    public void moveSettlement(Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {

        if(!canPlaceSettlement(player, fromX, fromY))
            return;

        if (map.at(fromX, fromY).occupiedBy != player || !canPlaceSettlementInTerrain(player, terrain, toX, toY))
            return;

        // Take token from player if settlement was last one on special place
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {

            TileType token = map.specialPlaceInSurrounding(fromX, fromY);

            if (player.playerHasTokenLeft(token))
                player.removeToken(token);
        }

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));

        TileType token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(toX, toY).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Moves a settlement to a new position and throws if the move is not valid.
     * Does not allow placing on water.
     * Token functions which allow placement on water handle their placement separately.
     *
     * @param player the player whose turn it is and who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     */
    public void moveSettlement(Player player, int fromX, int fromY, int toX, int toY) {

        checkIsPlayersTurn(player);

        if (map.at(fromX, fromY).occupiedBy != player)
            return;

        // Take token from player if settlement was last one on special place
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {

            TileType token = map.specialPlaceInSurrounding(fromX, fromY);

            if (player.playerHasTokenLeft(token))
                player.removeToken(token);
        }

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));

        TileType token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(toX, toY).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Checks if player can place anywhere on a terrain.
     *
     * @param player  the player to check for.
     * @param terrain the terrain to place settlement.
     * @param x       the x coordinate of the tile.
     * @param y       the y coordinate of the tile.
     * @return True if player can place settlement on the Terrain. False otherwise.
     */
    private boolean playerCanPlaceAnywhereOnTerrain(Player player, TileType terrain, int x, int y) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> allPlaceableTiles = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);

        if (allPlaceableTiles.isEmpty())
            return true;

        return false;
    }

    /**
     * check if a settlement is placeable within a terrain on a given position.
     *
     * @param player  the player to check for.
     * @param terrain the terrain to place settlement.
     * @param x       the x coordinate of the tile.
     * @param y       the y coordinate of the tile.
     * @return True if player can place a settlement on that position. False otherwise.
     */
    public boolean canPlaceSettlementInTerrain(Player player, TileType terrain, int x, int y) {

        if (!isPlayersTurn(player) || !player.hasRemainingSettlements())
            return false;

        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("parameter is not a terrain!");

        // terrain must match the terrain of the tile player wants to place a settlement
        if (!map.at(x, y).isTilePlaceable() && terrain != map.at(x, y).tileType)
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
     * Gets all possible positions to place a settlement for a player on a given terrain next to
     * other settlements.
     *
     * @param player  the player to check for.
     * @param terrain the terrain to check for.
     * @return All tiles that can be placed next to other settlements.
     */
    private Set<Tile> allPossibleSettlementPlacementsNextToOtherSettlement(Player player, TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> allowedTiles = new HashSet<>();
        Set<Tile> freeTiles = map.freeTilesOnTerrain(terrain);

        for (Tile freeTile : freeTiles) {
            if (map.settlementOfPlayerOnSurroundingTiles(player, freeTile.x, freeTile.y)) {
                allowedTiles.add(freeTile);
            }
        }
        return allowedTiles;
    }

    /**
     * Gives a preview of all possible tiles to place a settlement.
     *
     * @param player  the player to check for.
     * @param terrain the terrain the player has.
     * @return A set of all positions a player can place a settlement.
     */
    public Set<Tile> allPossibleSettlementPlacementsOnTerrain(Player player, TileType terrain) {
        Set<Tile> allPossiblePlacements = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);
        return (allPossiblePlacements.isEmpty()) ? map.freeTilesOnTerrain(terrain) : allPossiblePlacements;
    }

    /**
     * Gets all possible positions to place a settlement at the border of the map.
     *
     * @param player the player to check for.
     * @return All possible tiles to check on the border of the map.
     */
    public Set<Tile> allPossibleSettlementsOnBorder(Player player) {
        Set<Tile> placeableTilesAtBorder = new HashSet<>();

        for (Tile tile : map.freeTilesOnMapBorder(player)) {

            //TODO: function that gives all player placeable tiles on border
        }

        return placeableTilesAtBorder;
    }

    /**
     * Updates GUI preview for all possible tiles to place settlement
     *
     * @param player  the player.
     * @param terrain the terrain.
     * @return the set of Tiles where the player could place a settlement.
     */
    public Set<Tile> updatePreviewWithTerrain(Player player, TileType terrain) {
        //TODO: TileReadOnly
        Set<Tile> allPossiblePlacements = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);

        return (allPossiblePlacements.isEmpty()) ? map.freeTilesOnTerrain(terrain) : allPossiblePlacements;
    }

    /**
     * Preview all Settlements of a player on the map.
     *
     * @param player the player.
     * @return the set of tiles where the player has a settlement on.
     */
    public Set<Tile> previewAllPlayerSettlements(Player player) {
        //TODO: TileReadOnly
        return map.allSettlementsOfPlayerOnMap(player);
    }

    /**
     * Gives player a token and removes it from the special place.
     *
     * @param player the player the token is given to.
     * @param x      the x coordinate of the tile.
     * @param y      the y coordinate of the tile.
     */
    public void addToken(Player player, int x, int y) {
        //TODO: don't add token from the same place twice

        if (map.at(x, y).hasTokens()) {
            player.addToken(map.at(x, y).tileType);
            map.at(x, y).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Use one of the token to place or remove a settlement.
     *
     * @param tokenType the token to use.
     * @param player    the player that uses the token.
     * @param fromX     the x coordinate to place OR remove a settlement.
     * @param fromY     the y coordinate to place OR remove a settlement.
     * @param toX       the x coordinate of the settlement to move to.
     * @param toY       the y coordinate of the settlement to move to.
     */
    public void useToken(Player player, TileType tokenType, int fromX, int fromY, int toX, int toY) {
        if (!player.playerHasTokenLeft(tokenType))
            return;

        switch (tokenType) {

            case ORACLE:
                useTokenOracle(player, fromX, fromY);
                break;
            case FARM:
                useTokenFarm(player, fromX, fromY);
                break;
            case TAVERN:
                useTokenTavern(player, fromX, fromY);
                break;
            case TOWER:
                useTokenTower(player, fromX, fromY);
                break;
            case OASIS:
                useTokenOasis(player, fromX, fromY);
                break;
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
     * The first preview for using a token.
     *
     * @param player the player that gets a preview.
     * @param tokenType the token type.
     */
    public void previewToken(Player player, TileType tokenType) {
        if (!player.playerHasTokenLeft(tokenType))
            return;

        switch (tokenType) {

            case ORACLE:
                previewTokenOracle(player);
                break;
            case FARM:
                previewTokenFarm(player);
                break;
            case TAVERN:
                previewTokenTavern(player);
                break;
            case TOWER:
                previewTokenTower(player);
                break;
            case OASIS:
                previewTokenOasis(player);
                break;

                // Following three Tokens have one preview for selecting a token and one for placing one.
            case HARBOR:
                previewAllPlayerSettlements(player);
                break;
            case PADDOCK:
                previewAllPlayerSettlements(player);
                break;
            case BARN:
                previewAllPlayerSettlements(player);
                break;
            default:
                return;
        }
    }


    /**
     * Use the oracle token. The player is allowed to place an extra settlement on a tile that has the same type as
     * the players' terrain card.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenOracle(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, player.terrainCard, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.ORACLE);
        }
    }

    /**
     * Updates map preview before using oracle token.
     *
     * @param player the player.
     */
    private void previewTokenOracle(Player player) {
        updatePreviewWithTerrain(player, player.terrainCard);
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenFarm(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, TileType.GRAS, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.FARM);
        }
    }

    /**
     * Updates map preview before using farm token.
     *
     * @param player the player.
     */
    private void previewTokenFarm(Player player) {
        updatePreviewWithTerrain(player, TileType.GRAS);
    }

    /**
     * Use the Tavern token. The player can place an extra settlement at the front or back of a
     * chain of settlements that is owned by the player.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenTavern(Player player, int x, int y) {
        if (!canPlaceSettlement(player, x, y) || !map.freeTileIsInFrontOrBackOfAChain(player, map.at(x, y)))
            return;

        placeSettlement(player, x, y);
        player.removeToken(TileType.TAVERN);
    }

    /**
     * Updates the preview for tavern token.
     *
     * @param player the player to update for.
     * @return the set of tiles where the player could place a settlement.
     */
    private Set<Tile> previewTokenTavern(Player player) {
        //TODO: TileReadOnly
        return map.allFreeTilesInFrontOrBackOfAChain(player);
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenTower(Player player, int x, int y) {
        if (canPlaceSettlement(player, x, y) || !map.at(x, y).isAtBorder(map))
            return;

        placeSettlement(player, x, y);
        player.removeToken(TileType.TOWER);

    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player the player to update for.
     * @return the set of tiles where the player could place a settlement.
     */
    private Set<Tile> previewTokenTower(Player player) {
        //TODO: TileReadOnly

        Set<Tile> allPlaceableTiles = new HashSet<>();

        for (Tile tile : map.allFreeTilesOnBorderOfMap()) {
            if (tile.isTilePlaceable())
                allPlaceableTiles.add(tile);
        }
        return allPlaceableTiles;
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenOasis(Player player, int x, int y) {
        if (canPlaceSettlementInTerrain(player, TileType.DESERT, x, y)) {
            placeSettlement(player, x, y);
            player.removeToken(TileType.OASIS);
        }
    }

    /**
     * Updates map preview before using farm token Oasis.
     *
     * @param player the player who wants that preview.
     */
    private void previewTokenOasis(Player player) {
        updatePreviewWithTerrain(player, TileType.DESERT);
    }

    // TODO: implement, parameters should match the protocol message format
    private void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
        player.removeToken(TileType.HARBOR);
    }

    //TODO: implement
    public void previewTokenHarbor() {

    }

    /**
     * Use Paddock token. The player can move a settlement two tiles in horizontal or diagonal
     * direction.
     *
     * @param player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     */
    private void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
        if (canPlaceSettlement(player, toX, toY)) {
            player.removeToken(TileType.PADDOCK);
            moveSettlement(player, fromX, fromY, toX, toY);
        }
    }

    /**
     * Preview for token Paddock AFTER selecting a settlement.
     *
     * @param player the player.
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @return
     */
    public Set<Tile> previewTokenPaddockPlaceSettlement(Player player, int x, int y) {
        if (!map.at(x, y).isOccupiedByPlayer(player))
            throw new InvalidParameterException("player does not own a settlement at this place");

        return map.oneTileSkippedSurroundingTiles(x, y);
    }

    /**
     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
     *
     * @param player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     */
    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
        if (canPlaceSettlement(player, toX, toY)) {
            player.removeToken(TileType.BARN);
            moveSettlement(player, player.terrainCard, fromX, fromY, toX, toY);
        }
    }

    /**
     * Preview for token Barn AFTER selecting a settlement.
     *
     * @param player the player.
     * @return
     */
    private Set<Tile> previewTokenBarnPlaceSettlement(Player player) {
        //TODO: TileReadOnly
        return allPossibleSettlementPlacementsOnTerrain(player, player.terrainCard);
    }

    /**
     * Set map of the game.
     *
     * @param map the map to set.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Set players for the game.
     *
     * @param newPlayers the players to set for the game.
     */
    public void setPlayers(Player[] newPlayers) {
        playersMap.clear();
        players = newPlayers;
        for (var player : newPlayers) {
            playersMap.put(player.ID, player);
        }
    }

    /**
     * Sets the turn of the next player.
     *
     * @param newPlayer the next player for the turn.
     */
    public void setCurrentPlayer(Player newPlayer) {
        currentPlayer = newPlayer;
    }

    /**
     * Set win conditions for the game.
     *
     * @param newWinConditions the win conditions to set.
     */
    public void setWinConditions(WinCondition[] newWinConditions) {
        winConditions = newWinConditions;
    }

    /**
     * Set terrain card of current turn.
     *
     * @param newTerrainCard the current terrain type of the game.
     */
    public void setTerrainCardOfTurn(TileType newTerrainCard) {
        currentPlayer.terrainCard = newTerrainCard;
    }

    /**
     * Translates player id to the player object.
     *
     * @param playerID the id to switch.
     * @return the player object to return.
     */
    public Player playerIDtoObject(int playerID) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].ID == playerID) {
                return players[i];
            }
        }
        return null;
    }

    /**
     * Gets the myGameReply field.
     *
     * @return myGameReply field.
     */
    public MyGameReply getMyGameReply() {
        return myGameReply;
    }

    /**
     * Set game info.
     *
     * @param myGameReply the game info to set.
     */
    public void setGameInfo(MyGameReply myGameReply) {
        this.myGameReply = myGameReply;
        timeLimit = myGameReply.timeLimit();
        turnLimit = myGameReply.turnLimit();
        playerLimit = myGameReply.playerLimit();
    }

    /**
     * Gets the map of the game.
     *
     * @return the map.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Gets the players of this game.
     *
     * @return all the players of this game.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Gets the WinConditions of this game.
     *
     * @return the WinConditions.
     */
    public WinCondition[] getWinConditions() {
        return winConditions;
    }

    /**
     * Gets the terrain card of current turn.
     *
     * @return the terrain card of current turn.
     */
    public TileType getTerrainCard() {
        return terrainCard;
    }

    /**
     * Gets the current player.
     *
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
