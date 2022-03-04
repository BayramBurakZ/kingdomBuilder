package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains all the information of a game hosted on the server.
 */
public class Game {

    //region constants
    /**
     * Represents how many settlements can be placed with the basic turn.
     */
    public static final int SETTLEMENTS_PER_TURN = 3;

    /**
     * Represents the amount of settlements with which the player starts.
     */
    public static final int DEFAULT_STARTING_SETTLEMENTS = 40;

    //endregion constants

    /**
     * Starts the next turn for the specified player.
     *
     * @param player the player whose turn it is next.
     */
    public static void startTurn(Player player) {
        player.startTurn();
    }

    // TODO: maybe use this later
    public static void endTurn() {
        // placeholder, in case it's needed later
        //
        // maybe use startTurn() to set the player's terrain card as soon as it is known
        // and use endTurn to actually set currentPlayer to the next player
    }

    /**
     * Checks if player can place settlement without the context of a terrain that is only usable with tower,
     * paddock or tavern token.
     *
     * @param gameMap the map.
     * @param player the player that wants to place a settlement.
     * @param x      the x coordinate of tile to place a settlement.
     * @param y      the y coordinate of tile to place a settlement.
     * @return true if settlement can be placed on tile. False otherwise.
     */
    protected static boolean canUseBasicTurn(GameMap gameMap, Player player, int x, int y) {
        return canUseBasicTurn(gameMap, player, player.getTerrainCard(), x, y);
    }

    /**
     * Checks if player can place settlement on a terrain.
     *
     * @param gameMap the map.
     * @param player  the player that wants to place a settlement.
     * @param terrain the terrain to place the settlement.
     * @param x       the x coordinate of settlement to place.
     * @param y       the y coordinate of settlement to place.
     * @return true if settlement can be placed on terrain. False otherwise.
     */
    protected static boolean canUseBasicTurn(GameMap gameMap, Player player, TileType terrain, int x, int y) {
        return gameMap.isWithinBounds(x, y)
                && !gameMap.at(x, y).isBlocked()
                && player.hasRemainingSettlements()
                && gameMap.at(x, y).tileType == terrain
                && (gameMap.at(x, y).hasSurroundingSettlement(gameMap, player)
                || gameMap.getAllPlaceableTilesNextToSettlements(player, terrain).isEmpty());
    }

    /**
     * Checks if the given settlement can be moved without context of terrain.
     *
     * @param gameMap the map.
     * @param player the player whose turn it is and who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     * @return true if settlement can be moved. False otherwise.
     */
    protected static boolean canMoveSettlement(GameMap gameMap, Player player, int fromX, int fromY, int toX, int toY) {
        return (gameMap.at(fromX, fromY).occupiedBy == player || canUseBasicTurn(gameMap, player, toX, toY));
    }

    /**
     * Checks if the given settlement can be moved with context of terrain.
     *
     * @param gameMap the map.
     * @param player  the player whose turn it is and who owns the settlement.
     * @param terrain the terrain where the settlement will be placed.
     * @param fromX   the old horizontal position of the settlement on the map.
     * @param fromY   the old vertical position of the settlement on the map.
     * @param toX     the new horizontal position of the settlement on the map.
     * @param toY     the new vertical position of the settlement on the map.
     * @return true if settlement can be placed at that position. False otherwise.
     */
    protected static boolean canMoveSettlement(GameMap gameMap, Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {
        return (gameMap.at(fromX, fromY).occupiedBy == player || canUseBasicTurn(gameMap, player, terrain, toX, toY));
    }

    //TODO: Doc check!
    /**
     * Show all possible placements at the start of the turn.
     *
     * @param gameMap the map.
     *
     * @return all possible tiles at the start of the game.
     */
    public static Set<Tile> allBasicTurnTiles(GameMap gameMap, Player player) {
        return (player.remainingSettlementsOfTurn <= 0) ?
                new HashSet<>() : gameMap.getAllPlaceableTiles(player, player.getTerrainCard());
    }

    /**
     * Returns all possible places with using an oracle token.
     *
     * @param gameMap the map.
     * @param player the player that is using the token.
     *
     * @return the set of tiles where the player could place a settlement using the oracle token.
     */
    public static Set<Tile> allTokenOracleTiles(GameMap gameMap, Player player) {
        if (player.getRemainingSettlements() <= 0)
            return new HashSet<>();

        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.ORACLE)) ?
                new HashSet<>() : gameMap.getAllPlaceableTiles(player, player.getTerrainCard());
    }

    /**
     * Returns all possible places with using a farm token.
     *
     * @param gameMap the map.
     * @param player the player.
     *
     * @return the set of tiles where the player could place a settlement with using the farm token.
     */
    public static Set<Tile> allTokenFarmTiles(GameMap gameMap, Player player) {
        if (player.getRemainingSettlements() <= 0)
            return new HashSet<>();

        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.FARM) ?
                new HashSet<>() : gameMap.getAllPlaceableTiles(player, TileType.GRAS));
    }

    /**
     * Returns all possible places with using a tavern token.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     *
     * @return the set of tiles where the player could place a settlement with using the tavern token.
     */
    public static Set<Tile> allTokenTavernTiles(GameMap gameMap, Player player) {
        if (player.getRemainingSettlements() <= 0)
            return new HashSet<>();

        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.TAVERN)) ?
                new HashSet<>()
                : gameMap.stream().filter(tile -> !tile.isBlocked()
                && tile.isAtEndOfAChain(gameMap, player)).collect(Collectors.toSet());
    }

    /**
     * Returns all possible places with using a tower token.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     *
     * @return the set of tiles where the player could place a settlement with using the tower token.
     */
    public static Set<Tile> allTokenTowerTiles(GameMap gameMap, Player player) {
        if (player.getRemainingSettlements() <= 0)
            return new HashSet<>();

        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.TOWER)) ?
                new HashSet<>() : gameMap.getPlaceableTilesAtBorder(player);
    }

    /**
     * Returns all possible places with using an oasis token.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     *
     * @return the set of tiles where the player could place a settlement with using the Oasis Token.
     */
    public static Set<Tile> allTokenOasisTiles(GameMap gameMap, Player player) {
        if (player.getRemainingSettlements() <= 0)
            return new HashSet<>();

        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.OASIS)) ?
                new HashSet<>() : gameMap.getAllPlaceableTiles(player, TileType.DESERT);
    }

    /**
     * Returns all possible places with using an oracle token AFTER selecting a settlement to move.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     *
     * @return all tiles that are placeable with token harbor.
     */
    public static Set<Tile> allTokenHarborTiles(GameMap gameMap, Player player, boolean highlightDestination) {
        if (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.HARBOR))
            return new HashSet<>();

        Set<Tile> allPlaceableTilesWater = gameMap.getTiles(TileType.WATER).stream().filter(
                tile -> tile.tileType == TileType.WATER
                        && tile.occupiedBy == null
                        && tile.hasSurroundingSettlement(gameMap, player)).collect(Collectors.toSet());

        allPlaceableTilesWater = allPlaceableTilesWater.isEmpty() ?
                gameMap.getTiles(TileType.WATER).stream().filter(t -> t.occupiedBy == null ).collect(Collectors.toSet())
                : allPlaceableTilesWater;

        //TODO: Revisit all functions harbor is using
        return highlightDestination ? allPlaceableTilesWater : gameMap.getSettlements(player);
    }

    /**
     * Returns all possible places with using a harbor token.
     *
     * @param gameMap the map.
     * @param player the player whose turn it is.
     * @return all tiles that a paddock token can be used on.
     */
    public static Set<Tile> allTokenPaddockTiles(GameMap gameMap, Player player) {
        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.PADDOCK)) ?
                new HashSet<>() : gameMap.getSettlements(player);
    }

    /**
     * Returns all possible places with using a paddock token AFTER selecting a settlement to move.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     * @param fromX  the x-coordinate.
     * @param fromY  the y-coordinate.
     * @return all tiles that are placeable with token paddock.
     * @throws InvalidParameterException when player does not own a settlement at given position.
     */
    public static Set<Tile> allTokenPaddockTiles(GameMap gameMap, Player player, int fromX, int fromY) {
        // TODO: maybe throw or warning if the settlement at (fromX,fromY) doesn't match the specified player
        return (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.PADDOCK)) ?
                new HashSet<>() : gameMap.at(fromX, fromY).surroundingTilesPaddock(gameMap);
    }

    /**
     *Returns all possible places with using a barn token AFTER selecting a settlement to move.
     *
     * @param gameMap the map.
     * @param player the player to update for.
     *
     * @return all tiles that are placeable with token barn.
     */
    public static Set<Tile> allTokenBarnTiles(GameMap gameMap, Player player, boolean highlightDestination) {
        if (player.getCurrentTurnState() == TurnState.BASIC_TURN
                || !player.playerHasTokenLeft(TileType.BARN))
            return new HashSet<>();

        return highlightDestination ?
                gameMap.getAllPlaceableTiles(player, player.getTerrainCard()) : gameMap.getSettlements(player);
    }

    /**
     * Checks for special places surrounding the given coordinates and adds their tokens to the specified player
     * regardless of context.
     *
     * @param gameMap the map.
     * @param player the player who receives the tokens.
     * @param x      the x coordinate to check for surrounding special places.
     * @param y      the y coordinate to check for surrounding special places.
     */
    public static void unsafeCheckForTokens(GameMap gameMap, Player player, int x, int y) {
        Tile originTile = gameMap.at(x, y);
        Set<Tile> specialPlaces = originTile.surroundingSpecialPlaces(gameMap);

        // Player gets a token if settlement is next to special place
        for (var specialPlace : specialPlaces) {
            player.addToken(specialPlace);
        }
    }

    /**
     * Removes the specified token from the specified player regardless of context.
     *
     * @param gameMap the map.
     * @param player the player who receives the tokens.
     * @param x      the x coordinate of the special places.
     * @param y      the y coordinate of the special places.
     */
    public static void unsafeRemoveToken(GameMap gameMap, Player player, int x, int y) {
        Tile specialPlace = gameMap.at(x, y);
        player.removeToken(specialPlace);
    }

    /**
     * Places a settlement regardless of context.
     *
     * @param gameMap the map.
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player who owns the settlement.
     */
    public static void unsafePlaceSettlement(GameMap gameMap, Player player, int x, int y) {
        gameMap.at(x, y).placeSettlement(player);
        player.decrementRemainingSettlements();
    }

    /**
     * Moves a settlement to a new position regardless of context.
     *
     * @param gameMap the map.
     * @param player the player who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     */
    public static void unsafeMoveSettlement(GameMap gameMap, Player player, int fromX, int fromY, int toX, int toY) {
        if (gameMap.at(fromX, fromY).occupiedBy() != player)
            System.out.println("Moved settlement didn't match actual settlement!");
        gameMap.at(fromX, fromY).moveSettlement(gameMap.at(toX, toY));
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param gameMap the map.
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    public static void useBasicTurn(GameMap gameMap, Player player, int x, int y) {
        if (!canUseBasicTurn(gameMap, player, player.getTerrainCard(), x, y))
            throw new RuntimeException("Can't place settlement on"
                    + " tile: " + gameMap.at(x, y).tileType + " at " + x + "," + y);

        // simply read server message
        //unsafePlaceSettlement(player, x, y);
        player.useBasicTurn();
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
    public static void useTokenOracle(Player player, int x, int y) {
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
    public static void useTokenFarm(Player player, int x, int y) {
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
    public static void useTokenTavern(Player player, int x, int y) {
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
    public static void useTokenTower(Player player, int x, int y) {
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
    public static void useTokenOasis(Player player, int x, int y) {
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
    public static void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
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
    public static void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
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
    public static void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
        // simply read server message
        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
        player.useToken(TileType.BARN);
    }

    /**
     * Calculates the score for a specific player on the given map with the given WinConditions.
     * @param gameMap the map.
     * @param player the player for which the score should be calculated.
     * @param winConditions the winConditions
     *
     * @return the score for the specified player.
     */
    // TODO: Call the method
    public static int calculateScore(GameMap gameMap, Player player, List<WinCondition> winConditions) {
        // TODO: remove debug messages when everything works fine
        int score = 0;
        for (WinCondition c : winConditions) {
            switch(c) {
                case LORDS -> {
                    int tmp = scoreLord(gameMap, player);
                    System.out.println("Points for Lord: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case MINER -> {
                    int tmp = scoreMiner(gameMap, player);
                    System.out.println("Points for Miner: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case FARMER -> {
                    int tmp =  scoreFarmer(gameMap, player);
                    System.out.println("Points for Farmer: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case FISHER -> {
                    int tmp = scoreFisher(gameMap, player);
                    System.out.println("Points for Fisher: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case KNIGHT -> {
                    int tmp = scoreKnight(gameMap, player);
                    System.out.println("Points for Knight: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case WORKER -> {
                    int tmp = scoreWorker(gameMap, player);
                    System.out.println("Points for Worker: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case CITIZEN -> {
                    int tmp = scoreCitizen(gameMap, player);
                    System.out.println("Points for Citizen: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case EXPLORER -> {
                    int tmp = scoreExplorer(gameMap, player);
                    System.out.println("Points for Explorer: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case MERCHANT -> {
                    int tmp = scoreMerchant(gameMap, player);
                    System.out.println("Points for Merchant: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
                case ANCHORITE -> {
                    int tmp = scoreAnchorite(gameMap, player);
                    System.out.println("Points for Anchorite: " + tmp + " for player: " + player.name);
                    score += tmp;
                }
            }
        }

        return score + scoreCastles(gameMap, player);
    }

    static int scoreCastles(GameMap gameMap, Player player) {
        Set<Tile> castles = gameMap.stream().filter(t -> t.tileType == TileType.CASTLE).collect(Collectors.toSet());

        int castleScore = 0;
        for (Tile castle : castles) {
            if (castle.hasSurroundingSettlement(gameMap, player)) {
                castleScore += 3;
            }
        }
        System.out.println("Points for Castles: " + castleScore);

        return castleScore;
    }

    /**
     * Calculates the score for the given player on the given map with the win condition "anchorite".
     * This win conditions gives one point for every group of settlements.
     *
     * @param gameMap the map on which the score should be calculated.
     * @param player the player for which the score should be calculated.
     *
     * @return the score for the win condition "anchorite".
     */
    static int scoreAnchorite(GameMap gameMap, Player player)
    {
        Set<Tile> settlements = gameMap.getSettlements(player);

        int score = 0;

        Set<Tile> group = new HashSet<>();

        for (Tile t : settlements) {
            if (group.contains(t)) {
                continue;
            }
            gameMap.getSettlementGroup(group, player, t.x, t.y);
            score++;
        }

        return score;
    }

    /**
     * Calculates the score for the given player on the given map with the win condition "citizen".
     * This win conditions gives one point for two settlements in the biggest group of settlements.
     *
     * @param gameMap the map on which the score should be calculated.
     * @param player the player for which the score should be calculated.
     *
     * @return the score for the win condition "citizen".
     */
    static int scoreCitizen(GameMap gameMap, Player player)
    {
        Set<Tile> settlements = gameMap.getSettlements(player);

        int biggestSettlementSize = -1;

        Set<Tile> group = new HashSet<>();
        int prevGroupSize = group.size();

        for (Tile t : settlements) {
            if (group.contains(t)) {
                continue;
            }
            gameMap.getSettlementGroup(group, player, t.x, t.y);
            if ((group.size() - prevGroupSize) > biggestSettlementSize)
                biggestSettlementSize = group.size() - prevGroupSize;

            prevGroupSize = group.size();
        }

        int score = (int) (biggestSettlementSize/2);

        return score;
    }

    /**
     * Calculates the score for the given player on the given map with the win condition "explorer".
     * This win conditions gives one point for every horizontal line with at least one settlement.
     *
     * @param gameMap the map on which the score should be calculated.
     * @param player the player for which the score should be calculated.
     *
     * @return the score for the win condition "explorer".
     */
    static int scoreExplorer(GameMap gameMap, Player player)
    {
        int score = 0;

        for (int y = 0; y < gameMap.mapWidth; y++) {
            for (int x = 0; x < gameMap.mapWidth; x++) {
                if (gameMap.at(x, y).occupiedBy == player) {
                    score++;
                    break;
                }
            }
        }
        return score;
    }

    /**
     * Calculates the score for the given player on the given map with the win condition "knight".
     * This win conditions gives two points for the every settlement on the horizontal line with the
     * most settlement of your own.
     *
     * @param gameMap the map on which the score should be calculated.
     * @param player the player for which the score should be calculated.
     *
     * @return the score for the win condition "knight".
     */
    static int scoreKnight(GameMap gameMap, Player player)
    {
        int highestAmount = -1;

        for (int y = 0; y < gameMap.mapWidth; y++) {
            int settlementsPerLine = 0;
            for (int x = 0; x < gameMap.mapWidth; x++) {
                if (gameMap.at(x, y).occupiedBy == player) {
                    settlementsPerLine++;
                }
            }

            if(settlementsPerLine > highestAmount) {
                highestAmount = settlementsPerLine;
            }
        }

        return highestAmount *2;
    }

    // TODO: JavaDoc
    static int scoreMerchant(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }

    // TODO: JavaDoc
    static int scoreLord(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }

    // TODO: JavaDoc
    static int scoreFarmer(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }

    // TODO: JavaDoc
    static int scoreWorker(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }

    // TODO: JavaDoc
    static int scoreFisher(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }

    // TODO: JavaDoc
    static int scoreMiner(GameMap gameMap, Player player)
    {
        return 0; // TODO: Implementation
    }
}
