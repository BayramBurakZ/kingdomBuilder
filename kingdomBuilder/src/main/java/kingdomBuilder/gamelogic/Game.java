package kingdomBuilder.gamelogic;

import kingdomBuilder.network.protocol.MyGameReply;

import java.security.InvalidParameterException;
import java.util.*;

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

    /**
     * Internal data of the map
     */
    private Map map;

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
     * An array of the players playing in the game.
     */
    private Player[] players;

    public Player currentPlayer;

    /**
     * A map of the players playing in the game.
     */
    public final HashMap<Integer, Player> playersMap = new HashMap<>();

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

    // TODO: JavaDoc
    public void startTurn(int clientId, TileType terrainCard) {
        startTurn(playerIDtoObject(clientId), terrainCard);
    }

    // TODO: JavaDoc
    public void startTurn(Player player, TileType terrainCard) {
        currentPlayer = player;
        player.remainingSettlements = 3;
        player.startTurn(terrainCard);
    }

    // TODO: JavaDoc
    public void endTurn() {
        // placeholder, in case it's needed later
    }

    /**
     * Show all possible placements at the start of the turn.
     *
     * @return all possible tiles at the start of the game.
     */
    public Set<Tile> canPlaceSettlementsAll() {
        return updatePreviewWithTerrain(currentPlayer, currentPlayer.getTerrainCard());
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
     * Checks if player can place settlement without the context of a terrain that is only usable with tower,
     * paddock or tavern token.
     *
     * @param player the player that wants to place a settlement.
     * @param x      the x coordinate of tile to place a settlement.
     * @param y      the y coordiante of tile to place a settlement.
     * @return true if settlement can be placed on tile. False otherwise.
     */
    private boolean canPlaceSettlement(Player player, int x, int y) {
        return map.isWithinBounds(x, y)
                && map.at(x, y).isTilePlaceable()
                && isPlayersTurn(player)
                && player.hasRemainingSettlements();
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    private void placeSettlement(Player player, int x, int y) {
        if (!canPlaceSettlement(player, x, y))
            throw new RuntimeException("can't place settlement");

        map.at(x, y).placeSettlement(player);
        player.remainingSettlements--;

        Tile token = map.specialPlaceInSurrounding(x, y);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(x, y).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Checks if settlement can be moved without context of terrain.
     *
     * @param player the player whose turn it is and who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     * @return true if settlement can be moved. False otherwise.
     */
    private boolean canMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
        return (map.at(fromX, fromY).occupiedBy == player || canPlaceSettlement(player, toX, toY));
    }

    /**
     * Moves a settlement to a new position without context of a terrain.
     *
     * @param player the player whose turn it is and who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     */
    public void moveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
        if (!canMoveSettlement(player, fromX, fromY, toX, toY))
            throw new RuntimeException("can't move settlement");

        // Take token from player if settlement was last one on special place
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {

            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
            player.removeToken(token);
        }

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));

        Tile token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(toX, toY).takeTokenFromSpecialPlace();
        }
    }

    /**
     * Checks if player can place settlement on a terrain.
     *
     * @param player  the player that wants to place a settlement.
     * @param terrain the terrain to place the settlement.
     * @param x       the x coordinate of settlement to place.
     * @param y       the y coordinate of settlement to place.
     * @return true if settlement can be placed on terrain. False otherwise.
     */
    private boolean canPlaceSettlementOnTerrain(Player player, TileType terrain, int x, int y) {
        if (!map.isWithinBounds(x, y)
                || !map.at(x, y).isTilePlaceable()
                || !isPlayersTurn(player)
                || !player.hasRemainingSettlements()
                || map.at(x, y).tileType != terrain)
            return false;

        // Settlement is next to another settlement on same terrain.
        boolean hasSurroundingSettlementOnTerrain =
                map.settlementOfPlayerOnSurroundingTiles(player, terrain, x, y);

        // Player has no other free places on terrain and can place anywhere within terrain.
        boolean noFreeTilesNextToSettlements =
                allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain).isEmpty();

        return hasSurroundingSettlementOnTerrain || noFreeTilesNextToSettlements;
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    public void placeSettlementOnTerrain(Player player, TileType terrain, int x, int y) {
        if (!canPlaceSettlementOnTerrain(player, terrain, x, y))
            throw new RuntimeException("Can't place settlement on terrain: " + map.at(x,y).tileType + " at " + x + "," + y + " tile: " + map.at(x,y).tileType + " at " + map.at(x,y).x + "," + map.at(x,y).y);

        map.at(x, y).placeSettlement(player);
        player.remainingSettlements--;
        player.remainingSettlementsOfTurn--;

        Tile token = map.specialPlaceInSurrounding(x, y);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(x, y).takeTokenFromSpecialPlace();
        }
    }

    /**
     * @param player  the player whose turn it is and who owns the settlement.
     * @param terrain the terrain where the settlement will be placed.
     * @param fromX   the old horizontal position of the settlement on the map.
     * @param fromY   the old vertical position of the settlement on the map.
     * @param toX     the new horizontal position of the settlement on the map.
     * @param toY     the new vertical position of the settlement on the map.
     * @return true if settlement can be placed at that position. False otherwise.
     */
    private boolean canMoveSettlementOnTerrain(Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {
        return (map.at(fromX, fromY).occupiedBy == player || canPlaceSettlementOnTerrain(player, terrain, toX, toY));
    }

    /**
     * Moves a settlement to a new position within a given terrain.
     *
     * @param player  the player whose turn it is and who owns the settlement.
     * @param terrain the terrain it is getting placed.
     * @param fromX   the old horizontal position of the settlement on the map.
     * @param fromY   the old vertical position of the settlement on the map.
     * @param toX     the new horizontal position of the settlement on the map.
     * @param toY     the new vertical position of the settlement on the map.
     */
    public void moveSettlementOnTerrain(Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {

        if (!canMoveSettlementOnTerrain(player, terrain, fromX, fromY, toX, toY))
            throw new RuntimeException("can't move settlement on terrain");

        // Take token from player if settlement was last one on special place
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {

            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
            player.removeToken(token);
        }

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));

        Tile token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(toX, toY).takeTokenFromSpecialPlace();
        }
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
    private Set<Tile> allPossibleSettlementPlacementsOnTerrain(Player player, TileType terrain) {
        Set<Tile> allPossiblePlacements = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);
        return (allPossiblePlacements.isEmpty()) ? map.freeTilesOnTerrain(terrain) : allPossiblePlacements;
    }

    /**
     * Gets all free tiles that are next to a players settlement.
     *
     * @param player the player of the settlements.
     * @return
     */
    public Set<Tile> allPossibleSettlementPlacementsOnBorderOfMap(Player player) {
        Set<Tile> allPossiblePlacementsAtBorder = new HashSet<>();


        for (Tile tile : map.allSettlementsOfPlayerOnBorderOfMap(player)) {
            if (tile.x == 0 || tile.x == map.mapWidth - 1) {

                if (map.isWithinBounds(tile.x, tile.y - 1)
                        && map.at(tile.x, tile.y - 1).isTilePlaceable()
                        && map.at(tile.x, tile.y - 1).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x, tile.y - 1));

                if (map.isWithinBounds(tile.x, tile.y + 1)
                        && map.at(tile.x, tile.y + 1).isTilePlaceable()
                        && map.at(tile.x, tile.y + 1).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x, tile.y + 1));
            }

            if (tile.y == 0 || tile.y == map.mapWidth - 1) {

                if (map.isWithinBounds(tile.x - 1, tile.y)
                        && map.at(tile.x + 1, tile.y).isTilePlaceable()
                        && map.at(tile.x - 1, tile.y).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x - 1, tile.y));

                if (map.isWithinBounds(tile.x + 1, tile.y)
                        && map.at(tile.x + 1, tile.y).isTilePlaceable()
                        && map.at(tile.x + 1, tile.y).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x + 1, tile.y));
            }
        }

        return allPossiblePlacementsAtBorder;
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

        if(player.remainingSettlementsOfTurn <=  0)
            return new HashSet<>();

        final Set<Tile> allPossiblePlacements = allPossibleSettlementPlacementsNextToOtherSettlement(player, terrain);
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
     * Use one of the token to place or move a settlement.
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
     * @param player    the player that gets a preview.
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
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use oracle token.
     */
    private void useTokenOracle(Player player, int x, int y) {

        if (!canPlaceSettlementOnTerrain(player, player.getTerrainCard(), x, y))
            throw new RuntimeException("can't use token oracle");


        //placeSettlement(player, x, y);
        player.useToken(TileType.ORACLE);
    }

    /**
     * Updates map preview before using oracle token.
     *
     * @param player the player that is using the token.
     */
    private void previewTokenOracle(Player player) {
        updatePreviewWithTerrain(player, player.getTerrainCard());
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use farm token.
     */
    private void useTokenFarm(Player player, int x, int y) {

        if (!canPlaceSettlementOnTerrain(player, TileType.GRAS, x, y))
            throw new RuntimeException("can't use token Farm");

        //placeSettlement(player, x, y);
        player.useToken(TileType.FARM);
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
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use tavern token.
     */
    private void useTokenTavern(Player player, int x, int y) {

        if (!canPlaceSettlement(player, x, y) || !map.tileIsInFrontOrBackOfAChain(player, map.at(x, y)))
            throw new RuntimeException("can't use token tavern");

        //placeSettlement(player, x, y);
        player.useToken(TileType.TAVERN);
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
     * @throws RuntimeException gets thrown when player can not use tower token.
     */
    private void useTokenTower(Player player, int x, int y) {

        if (!canPlaceSettlement(player, x, y)
                || !allPossibleSettlementPlacementsOnBorderOfMap(player).contains(map.at(x, y)))
            throw new RuntimeException("can't use token tower");

        //placeSettlement(player, x, y);
        player.useToken(TileType.TOWER);
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player the player to update for.
     * @return the set of tiles where the player could place a settlement.
     */
    private Set<Tile> previewTokenTower(Player player) {
        //TODO: TileReadOnly

        return allPossibleSettlementPlacementsOnBorderOfMap(player);
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use oasis token.
     */
    private void useTokenOasis(Player player, int x, int y) {

        if (canPlaceSettlementOnTerrain(player, TileType.DESERT, x, y))
            throw new RuntimeException("can't use token oasis");

        //placeSettlement(player, x, y);
        player.useToken(TileType.OASIS);
    }

    /**
     * Updates map preview before using farm token Oasis.
     *
     * @param player the player to update for.
     */
    private void previewTokenOasis(Player player) {
        updatePreviewWithTerrain(player, TileType.DESERT);
    }

    /**
     * Use Harbor token. The player can move a settlement from any tile to a water tile.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use harbor token.
     */
    private void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {

        if (canMoveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY))
            throw new RuntimeException("can't use token harbor");

        //moveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY);
        player.useToken(TileType.OASIS);
    }

    /**
     * Preview for token harbor AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @return all tiles that are placeable with token harbor.
     */
    public Set<Tile> previewTokenHarborPlaceSettlement(Player player) {
        //TODO: readonly tile
        return allPossibleSettlementPlacementsOnTerrain(player, TileType.WATER);
    }

    /**
     * Use Paddock token. The player can move a settlement two tiles in horizontal or diagonal
     * direction.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use paddock token.
     */
    private void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {

        if (canPlaceSettlement(player, toX, toY))
            throw new RuntimeException("can't use token paddock");

        //moveSettlement(player, fromX, fromY, toX, toY);
        player.useToken(TileType.PADDOCK);
    }

    /**
     * Preview for token Paddock AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @return all tiles that are placeable with token paddock.
     * @throws InvalidParameterException when player does not own a settlement at given position.
     */
    public Set<Tile> previewTokenPaddockPlaceSettlement(Player player, int x, int y) {

        if (!map.at(x, y).isOccupiedByPlayer(player))
            throw new InvalidParameterException("player does not own a settlement at this place");

        return map.oneTileSkippedSurroundingTiles(x, y);
    }

    /**
     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use barn token.
     */
    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {

        if (!canMoveSettlementOnTerrain(player, player.getTerrainCard(), fromX, fromY, toX, toY))
            throw new RuntimeException("can't use token paddock");

        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
        player.useToken(TileType.BARN);
    }

    /**
     * Preview for token Barn AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @return all tiles that are placeable with token barn.
     */
    public Set<Tile> previewTokenBarnPlaceSettlement(Player player) {
        //TODO: TileReadOnly
        return allPossibleSettlementPlacementsOnTerrain(player, player.getTerrainCard());
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
     * Set win conditions for the game.
     *
     * @param newWinConditions the win conditions to set.
     */
    public void setWinConditions(WinCondition[] newWinConditions) {
        winConditions = newWinConditions;
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

}
