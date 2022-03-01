package kingdomBuilder.gamelogic;

import kingdomBuilder.network.protocol.MyGameReply;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

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
    protected String gameName;

    /**
     * The description of the game.
     */
    protected String gameDescription;

    /**
     * The maximum amount of players that can play in the game.
     */
    protected int playerLimit;

    /**
     * The maximum amount of time a player can spend on each turn.
     */
    protected int timeLimit;

    /**
     * The maximum amount of turns the game can run for.
     */
    protected int turnLimit;

    /**
     * Internal data of the map
     */
    protected Map map;

    // Additional data for a game.
    /**
     * The ID of the client who created the game.
     */
    protected int hostID;

    /**
     * An array of the win conditions of the game.
     */
    protected WinCondition[] winConditions;

    /**
     * An array of the players playing in the game.
     */
    protected Player[] players;

    /**
     * Current player on turn.
     */
    public Player currentPlayer;

    /**
     * The currently selected token.
     */
    public TileType selectedToken;

    /**
     * A map of the players playing in the game.
     */
    public final HashMap<Integer, Player> playersMap = new HashMap<>();

    /**
     * The total amount of settlements a player has to place to end the game.
     */
    protected int startingSettlements;

    /**
     * The network message infos of the game.
     */
    protected MyGameReply myGameReply;

    /**
     * Represents the state if the game has started.
     */
    protected boolean gameRunning = false;

    /**
     * Constructs a new Game object.
     */
    public Game() {

    }

    /**
     * Starts the next turn for the specified player.
     *
     * @param clientId the client ID of the player.
     */
    public void startTurn(int clientId) {
        startTurn(playerIDtoObject(clientId));
    }

    /**
     * Starts the next turn for the specified player.
     *
     * @param player the player whose turn it is next.
     */
    public void startTurn(Player player) {
        currentPlayer = player;
        player.startTurn();
    }

    // TODO: maybe use this later
    public void endTurn() {
        // placeholder, in case it's needed later
        //
        // maybe use startTurn() to set the player's terrain card as soon as it is known
        // and use endTurn to actually set currentPlayer to the next player
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
     * Checks if player can place settlement without the context of a terrain that is only usable with tower,
     * paddock or tavern token.
     *
     * @param player the player that wants to place a settlement.
     * @param x      the x coordinate of tile to place a settlement.
     * @param y      the y coordiante of tile to place a settlement.
     * @return true if settlement can be placed on tile. False otherwise.
     */
    protected boolean canUseBasicTurn(Player player, int x, int y) {
        return canUseBasicTurn(player, player.getTerrainCard(), x, y);
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
    protected boolean canUseBasicTurn(Player player, TileType terrain, int x, int y) {
        return map.isWithinBounds(x, y)
                && !map.at(x, y).isBlocked()
                && isPlayersTurn(player)
                && player.hasRemainingSettlements()
                && map.at(x, y).tileType == terrain
                && (map.at(x, y).hasSurroundingSettlement(map, player)
                || map.getAllPlaceableTilesNextToSettlements(player, terrain).isEmpty());
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
    protected boolean canMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
        return (map.at(fromX, fromY).occupiedBy == player || canUseBasicTurn(player, toX, toY));
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
    protected boolean canMoveSettlement(Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {
        return (map.at(fromX, fromY).occupiedBy == player || canUseBasicTurn(player, terrain, toX, toY));
    }

    /**
     * Show all possible placements at the start of the turn.
     *
     * @return all possible tiles at the start of the game.
     */
    public Set<Tile> allBasicTurnTiles(Player player) {
        return (player.remainingSettlementsOfTurn <= 0) ?
                new HashSet<>() : map.getAllPlaceableTiles(player, player.getTerrainCard());
    }

    /**
     * Updates map preview before using oracle token.
     *
     * @param player the player that is using the token.
     */
    public Set<Tile> allTokenOracleTiles(Player player) {
        if (player.remainingSettlements <= 0)
            return new HashSet<>();

        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.ORACLE)) ?
                new HashSet<>() : map.getAllPlaceableTiles(player, player.getTerrainCard());
    }

    /**
     * Updates map preview before using farm token.
     *
     * @param player the player.
     */
    public Set<Tile> allTokenFarmTiles(Player player) {
        if (player.remainingSettlements <= 0)
            return new HashSet<>();

        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn)
                || !player.playerHasTokenLeft(TileType.FARM) ?
                new HashSet<>() : map.getAllPlaceableTiles(player, TileType.GRAS);
    }

    /**
     * Updates the preview for tavern token.
     *
     * @param player the player to update for.
     * @return the set of tiles where the player could place a settlement.
     */
    public Set<Tile> allTokenTavernTiles(Player player) {
        if (player.remainingSettlements <= 0)
            return new HashSet<>();

        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.TAVERN)) ?
                new HashSet<>()
                : map.stream().filter(tile -> !tile.isBlocked()
                && tile.isAtEndOfAChain(map, player)).collect(Collectors.toSet());
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player the player to update for.
     * @return the set of tiles where the player could place a settlement.
     */
    public Set<Tile> allTokenTowerTiles(Player player) {
        if (player.remainingSettlements <= 0)
            return new HashSet<>();

        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.TOWER)) ?
                new HashSet<>() : map.getPlaceableTilesAtBorder(player);
    }

    /**
     * Updates map preview before using farm token Oasis.
     *
     * @param player the player to update for.
     */
    public Set<Tile> allTokenOasisTiles(Player player) {
        if (player.remainingSettlements <= 0)
            return new HashSet<>();

        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.OASIS)) ?
                new HashSet<>() : map.getAllPlaceableTiles(player, TileType.DESERT);
    }

    /**
     * Preview for token harbor AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @return all tiles that are placeable with token harbor.
     */
    public Set<Tile> allTokenHarborTiles(Player player, boolean highlightDestination) {
        if (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.HARBOR))
            return new HashSet<>();

        Set<Tile> allPlaceableTilesWater = map.getTiles(TileType.WATER).stream().filter(
                tile -> tile.tileType == TileType.WATER
                        && tile.occupiedBy == null
                        && tile.hasSurroundingSettlement(map, player)).collect(Collectors.toSet());

        allPlaceableTilesWater = allPlaceableTilesWater.isEmpty() ?
                map.getTiles(TileType.WATER).stream().filter(t -> t.occupiedBy == null ).collect(Collectors.toSet())
                : allPlaceableTilesWater;

        //TODO: Revisit all functions harbor is using
        return highlightDestination ? allPlaceableTilesWater : map.getSettlements(player);
    }

    /**
     * Returns all tiles that a paddock token can be used on.
     *
     * @param player the player whose turn it is.
     * @return all tiles that a paddock token can be used on.
     */
    public Set<Tile> allTokenPaddockTiles(Player player) {
        return (!player.playerHasTokenLeft(TileType.PADDOCK)) ? new HashSet<>() : map.getSettlements(player);
    }

    /**
     * Preview for token Paddock AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @param fromX  the x-coordinate.
     * @param fromY  the y-coordinate.
     * @return all tiles that are placeable with token paddock.
     * @throws InvalidParameterException when player does not own a settlement at given position.
     */
    public Set<Tile> allTokenPaddockTiles(Player player, int fromX, int fromY) {
        // TODO: maybe throw or warning if the settlement at (fromX,fromY) doesn't match the specified player
        return (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.PADDOCK)) ?
                new HashSet<>() : map.at(fromX, fromY).surroundingTilesPaddock(map);
    }

    /**
     * Preview for token Barn AFTER selecting a settlement to move.
     *
     * @param player the player to update for.
     * @return all tiles that are placeable with token barn.
     */
    public Set<Tile> allTokenBarnTiles(Player player, boolean highlightDestination) {
        if (player.remainingSettlementsOfTurn > 0
                && player.remainingSettlementsOfTurn < player.remainingSettlementsAtStartOfTurn
                || !player.playerHasTokenLeft(TileType.BARN))
            return new HashSet<>();

        return highlightDestination ?
                map.getAllPlaceableTiles(player, player.getTerrainCard()) : map.getSettlements(player);
    }

    /**
     * Checks for special places surrounding the given coordinates and adds their tokens to the specified player
     * regardless of context.
     *
     * @param player the player who receives the tokens.
     * @param x      the x coordinate to check for surrounding special places.
     * @param y      the y coordinate to check for surrounding special places.
     */
    public void unsafeCheckForTokens(Player player, int x, int y) {
        Tile originTile = map.at(x, y);
        Set<Tile> specialPlaces = originTile.surroundingSpecialPlaces(map);

        // Player gets a token if settlement is next to special place
        for (var specialPlace : specialPlaces) {
            player.addToken(specialPlace);
        }
    }

    /**
     * Removes the specified token from the specified player regardless of context.
     *
     * @param player the player who receives the tokens.
     * @param x      the x coordinate of the special places.
     * @param y      the y coordinate of the special places.
     */
    public void unsafeRemoveToken(Player player, int x, int y) {
        Tile specialPlace = map.at(x, y);
        player.removeToken(specialPlace);
    }

    /**
     * Places a settlement regardless of context.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player who owns the settlement.
     */
    public void unsafePlaceSettlement(Player player, int x, int y) {
        map.at(x, y).placeSettlement(player);
        player.remainingSettlements--;
    }

    /**
     * Moves a settlement to a new position regardless of context.
     *
     * @param player the player who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     */
    public void unsafeMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
        if (map.at(fromX, fromY).occupiedBy() != player)
            System.out.println("Moved settlement didn't match actual settlement!");
        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    public void useBasicTurn(Player player, int x, int y) {
        if (!canUseBasicTurn(player, player.getTerrainCard(), x, y))
            throw new RuntimeException("Can't place settlement on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);

        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.remainingSettlementsOfTurn--; // do not subtract with token

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
    public void useTokenOracle(Player player, int x, int y) {
        /*if (!canUseBasicTurn(player, player.getTerrainCard(), x, y))
            throw new RuntimeException("Can't use token oracle on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);


         */
        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useToken(TileType.ORACLE);
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use farm token.
     */
    public void useTokenFarm(Player player, int x, int y) {
        /*
        if (!canUseBasicTurn(player, TileType.GRAS, x, y))
            throw new RuntimeException("Can't use token farm on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);


         */
        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useToken(TileType.FARM);
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
    public void useTokenTavern(Player player, int x, int y) {
        /*
        if (!canUseBasicTurn(player, x, y) || !map.at(x, y).isAtEndOfAChain(map, player))
            throw new RuntimeException("Can't use token tavern on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);


         */
        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useToken(TileType.TAVERN);
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use tower token.
     */
    public void useTokenTower(Player player, int x, int y) {
        /*
        if (!canUseBasicTurn(player, x, y)
                || !map.getPlaceableTilesAtBorder(player).contains(map.at(x, y)))
            throw new RuntimeException("Can't use token oracle on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);


         */
        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useToken(TileType.TOWER);
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use oasis token.
     */
    public void useTokenOasis(Player player, int x, int y) {
        /*
        if (canUseBasicTurn(player, TileType.DESERT, x, y))
            throw new RuntimeException("Can't use token oasis on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);


         */
        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useToken(TileType.OASIS);
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
    public void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
        /*
        if (canMoveSettlement(player, TileType.WATER, fromX, fromY, toX, toY))
            throw new RuntimeException("Can't use token harbor on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);


         */
        // simply read server message
        //moveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY);
        player.useToken(TileType.HARBOR);
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
    public void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
        /*
        if (canUseBasicTurn(player, toX, toY))
            throw new RuntimeException("Can't use token paddock on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);


         */
        // simply read server message
        //unsafeMoveSettlement(player, fromX, fromY, toX, toY);
        player.useToken(TileType.PADDOCK);
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
        /*
        if (!canMoveSettlement(player, player.getTerrainCard(), fromX, fromY, toX, toY))
            throw new RuntimeException("Can't use token barn on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);

         */

        // simply read server message
        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
        player.useToken(TileType.BARN);
    }

    // TODO: JavaDoc
    public int scoreWorker(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreFisher(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreMiner(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreAnchorite(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreCitizen(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreExplorer(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreKnight(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreMerchant(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreLord(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
    }

    // TODO: JavaDoc
    public int scoreFarmer(Player player)
    {
        return map.getSettlements(player).size(); // TODO: Implementation
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
