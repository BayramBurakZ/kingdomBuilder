package kingdomBuilder.gamelogic;

import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AIGame represents the AI.
 */
public class AIGame {

    /**
     * the map that is shared  between  all  players.
     */
    private GameMap gameMap;
    /**
     * the player that the  AI  controls.
     */
    public Player aiPlayer;

    /**
     * the win conditions of the current game.
     */
    private List<WinCondition> winConditions;

    /**
     * the difficulty of the AI.
     */
    private int difficulty;

    /**
     * The store of the game.
     */
    private final Store<KBState> store;

    /**
     * the constructor for AIGame.
     *
     * @param gameMap    the game map that is shared between all players.
     * @param difficulty the difficulty of the AI.
     */
    public AIGame(GameMap gameMap, int difficulty, Store<KBState> store) {
        this.gameMap = gameMap;
        this.difficulty = difficulty;
        this.store = store;
    }

    /**
     * selects the AI that is used for the game.
     *
     * @param terrain of the current turn.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> chooseAI(TileType terrain) {
        switch (difficulty) {
            case 1:
                return greedyPlacement(terrain);
            default:
                return randomPlacement(terrain);
        }
    }

    /**
     * AI that places settlements randomly without using token.
     *
     * @param terrain of the current turn.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> randomPlacement(TileType terrain) {

        List<ClientTurn> moves = new ArrayList(3);
        GameMap aiGameMap = new GameMap(gameMap);

        for (int i = 0; i < aiPlayer.remainingSettlementsOfTurn; i++) {

            List<Tile> placeable = aiGameMap.getAllPlaceableTiles(aiPlayer, terrain).toList();
            int index = (int) (Math.random() * (placeable.size()));
            Tile t = placeable.get(index);

            aiGameMap.at(t.x, t.y).placeSettlement(aiPlayer);
            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, t.x, t.y, -1, -1));
        }

        return moves;
    }

    /**
     * AI that uses greedy algorithm and uses token.
     * It calculates the best next placement for a settlement within a basic turn and with every token  that the  ai  has.
     *
     * @param terrain of the current turn.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> greedyPlacement(TileType terrain) {
        //TODO: somewhere trying to place without overall settlement left
        GameMap aiGameMap = new GameMap(gameMap);
        List<ClientTurn> moves = new ArrayList<>();
        boolean firstMoveOnSpecialPlace = false;
        int settlementsLeft = aiPlayer.getRemainingSettlements();

        Set<Tile> freeTiles = aiGameMap.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());
        Set<Tile> token = aiGameMap.getTiles().filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());

        // prefer a special place as first turn
        outerloop:
        for (Tile t : token) {
            for (Tile l : t.surroundingTiles(aiGameMap).collect(Collectors.toSet())) {
                if (!t.hasSurroundingSettlement(aiGameMap, aiPlayer) && freeTiles.contains(l)) {
                    firstMoveOnSpecialPlace = true;
                    moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, l.x, l.y, -1, -1));
                    System.out.println("BOT: Placing Basic Settlement on: x" + l.x + ", y: " + l.y);
                    aiGameMap.at(l.x, l.y).placeSettlement(aiPlayer);
                    settlementsLeft--;
                    break outerloop;
                }
            }
        }

        // make basic turn
        int currentScore = 0;
        int bestScore = Game.calculateScore(aiGameMap, aiPlayer, winConditions, store.getState().players());
        Tile bestTile = null;

        for (int i = 0; i < aiPlayer.remainingSettlementsOfTurn; i++) {
            if (firstMoveOnSpecialPlace) {
                firstMoveOnSpecialPlace = false;
                continue;
            }

            freeTiles = aiGameMap.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());

            for (Tile t : freeTiles) {
                aiGameMap.at(t.x, t.y).placeSettlement(aiPlayer);
                currentScore = Game.calculateScore(aiGameMap, aiPlayer, winConditions, store.getState().players());
                aiGameMap.at(t.x, t.y).removeSettlement();

                if (currentScore >= bestScore) {
                    bestScore = currentScore;
                    bestTile = t;
                }
            }

            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, bestTile.x, bestTile.y, -1, -1));
            System.out.println("BOT: Placing Basic Settlement on: x" + bestTile.x + ", y: " + bestTile.y);
            aiGameMap.at(bestTile.x, bestTile.y).placeSettlement(aiPlayer);
            settlementsLeft--;

            bestScore = 0;
            bestTile = null;
        }

        // use tokens
        outerloop2:
        for (TileType t : aiPlayer.getTokens().keySet()) {

            //TODO: !!!BUG!!! following token do not work!
            //if (t == TileType.TAVERN || t == TileType.PADDOCK || t == TileType.HARBOR || t == TileType.BARN)
            //    continue;

            int amountToken = aiPlayer.getTokens().get(t).getRemaining();

            for (int i = 0; i < amountToken; i++) {

                // move is allowed because it does not place a settlement
                if (settlementsLeft <= 0 && !(t == TileType.PADDOCK || t == TileType.HARBOR || t == TileType.BARN)) {
                    // search for more tokens that could be played
                    continue outerloop2;
                }

                ClientTurn move = calculateScoreToken(aiGameMap, aiPlayer, t);

                if (move != null) {
                    moves.add(move);
                    System.out.println(aiPlayer.name + " is using token: " + t + " with: fromX:" + move.x + " fromY:"
                            + move.y + " ToX:" + move.toX + " ToY:" + move.toY);
                    if (!(t == TileType.PADDOCK || t == TileType.HARBOR || t == TileType.BARN))
                        settlementsLeft--;
                }

            }
        }
        // debug message
        //System.out.println(settlementsLeft);
        return moves;
    }

    /**
     * Calculates all possible placements of a settlement with a token and return the move with the highest score.
     *
     * @param map    the map  for  the  AI.
     * @param player the player that  the  AI  controlls .
     * @param token  the token that is used.
     * @return the  best  move for  a  settlement with that token.
     */
    private ClientTurn calculateScoreToken(GameMap map, Player player, TileType token) {
        ClientTurn bestMove = null;

        Set<Tile> freeTiles;
        int currentScore = 0;
        int bestScore = Game.calculateScore(map, player, winConditions, store.getState().players());

        switch (token) {

            case HARBOR -> {
                Set<Tile> settlements =
                        Game.allTokenHarborTiles(map, player, false).collect(Collectors.toSet());

                for (Tile t : settlements) {
                    freeTiles = Game.allTokenHarborTiles(map, player, true).collect(Collectors.toSet());

                    for (Tile l : freeTiles) {
                        useTokenHarbor(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, store.getState().players());

                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                        }
                        undoToken(map, player, token, t.x, t.y, l.x, l.y);
                    }
                }
                if (bestMove == null)
                    return null;

                useTokenHarbor(map, player, bestMove.x, bestMove.y, bestMove.toX, bestMove.toY);
                return bestMove;
            }

            case PADDOCK -> {
                Set<Tile> settlements = Game.allTokenPaddockTiles(map, player).collect(Collectors.toSet());

                for (Tile t : settlements) {
                    freeTiles = Game.allTokenPaddockTiles(map, player, t.x, t.y).collect(Collectors.toSet());

                    for (Tile l : freeTiles) {
                        useTokenPaddock(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, store.getState().players());

                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                        }
                        undoToken(map, player, token, t.x, t.y, l.x, l.y);
                    }
                }

                if (bestMove == null)
                    return null;

                useTokenPaddock(map, player, bestMove.x, bestMove.y, bestMove.toX, bestMove.toY);
                return bestMove;
            }

            case BARN -> {
                Set<Tile> settlements =
                        Game.allTokenBarnTiles(map, player, false).collect(Collectors.toSet());

                for (Tile t : settlements) {
                    freeTiles = Game.allTokenBarnTiles(map, player, true).collect(Collectors.toSet());

                    for (Tile l : freeTiles) {
                        useTokenBarn(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, store.getState().players());

                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                        }
                        undoToken(map, player, token, t.x, t.y, l.x, l.y);
                    }
                }

                if (bestMove == null)
                    return null;

                useTokenBarn(map, player, bestMove.x, bestMove.y, bestMove.toX, bestMove.toY);
                return bestMove;
            }

            default -> {
                Tile bestTile = null;

                if (!TileType.tokenType.contains(token))
                    return null;

                freeTiles = allPlaceTokenTiles(map, player, token);

                if (freeTiles.isEmpty())
                    return null;

                for (Tile t : freeTiles) {
                    useToken(map, player, token, t.x, t.y);
                    currentScore = Game.calculateScore(map, player, winConditions, store.getState().players());

                    // always play a token
                    if (currentScore >= bestScore) {
                        bestScore = currentScore;
                        bestTile = t;
                    }
                    undoToken(map, player, token, t.x, t.y);
                }

                if (bestTile == null)
                    return null;

                useToken(map, player, token, bestTile.x, bestTile.y);
                return tokenToClientTurn(player, token, bestTile.x, bestTile.y, -1, -1);
            }
        }
    }

    /**
     * returns all possible placements with the  given  token that  only  places  settlements .
     *
     * @param map    the  map  for  the  AI.
     * @param player the  player  that the AI controls.
     * @param token  the token.
     * @return all  possible placements  with  the  given token.
     */
    private Set<Tile> allPlaceTokenTiles(GameMap map, Player player, TileType token) {
        return switch (token) {
            case ORACLE -> Game.allTokenOracleTiles(map, player).collect(Collectors.toSet());
            case FARM -> Game.allTokenFarmTiles(map, player).collect(Collectors.toSet());
            case TAVERN -> Game.allTokenTavernTiles(map, player).collect(Collectors.toSet());
            case TOWER -> Game.allTokenTowerTiles(map, player).collect(Collectors.toSet());
            case OASIS -> Game.allTokenOasisTiles(map, player).collect(Collectors.toSet());
            default -> new HashSet<>();
        };
    }

    /**
     * uses a token that only places from the AI.
     *
     * @param map    the map of the AI.
     * @param player the player that the AI controls.
     * @param token  the token that is being used.
     * @param x      the x coordinate.
     * @param y      the y coordinate.
     */
    private void useToken(GameMap map, Player player, TileType token, int x, int y) {
        switch (token) {
            case ORACLE -> useTokenOracle(map, player, x, y);
            case FARM -> useTokenFarm(map, player, x, y);
            case TAVERN -> useTokenTavern(map, player, x, y);
            case TOWER -> useTokenTower(map, player, x, y);
            case OASIS -> useTokenOasis(map, player, x, y);
        }
    }

    /**
     * returns a ClientTurn as a move with the token.
     *
     * @param player the player that the AI controls.
     * @param token  the token that is being used.
     * @param fromX  the x coordinate of the settlement that is moved.
     * @param fromY  the y coordinate of the settlement that is moved.
     * @param toX    the x coordinate of the new destination.
     * @param toY    the y coordinate of the new destination.
     * @return the ClientTurn as a move with the token.
     */
    private ClientTurn tokenToClientTurn(Player player, TileType token, int fromX, int fromY, int toX, int toY) {
        return switch (token) {
            case ORACLE -> new ClientTurn(player.ID, ClientTurn.TurnType.ORACLE, fromX, fromY, toX, toY);
            case FARM -> new ClientTurn(player.ID, ClientTurn.TurnType.FARM, fromX, fromY, toX, toY);
            case TAVERN -> new ClientTurn(player.ID, ClientTurn.TurnType.TAVERN, fromX, fromY, toX, toY);
            case TOWER -> new ClientTurn(player.ID, ClientTurn.TurnType.TOWER, fromX, fromY, toX, toY);
            case OASIS -> new ClientTurn(player.ID, ClientTurn.TurnType.OASIS, fromX, fromY, toX, toY);
            case HARBOR -> new ClientTurn(player.ID, ClientTurn.TurnType.HARBOR, fromX, fromY, toX, toY);
            case PADDOCK -> new ClientTurn(player.ID, ClientTurn.TurnType.PADDOCK, fromX, fromY, toX, toY);
            case BARN -> new ClientTurn(player.ID, ClientTurn.TurnType.BARN, fromX, fromY, toX, toY);
            default -> null;
        };
    }

    /**
     * undo the use of a token that only places by removing it from the map.
     *
     * @param map    the map of the AI.
     * @param player the player that the AI controls.
     * @param token  the token that is being used.
     * @param x      the x coordinate.
     * @param y      the y coordinate.
     */
    private void undoToken(GameMap map, Player player, TileType token, int x, int y) {
        map.at(x, y).removeSettlement();
        player.undoToken(token);
    }

    /**
     * undo the use of a token that only moves settlement by placing the settlement on its original tile.
     *
     * @param player the player that the AI controls.
     * @param token  the token that is being used.
     * @param fromX  the x coordinate of the settlement that is moved.
     * @param fromY  the y coordinate of the settlement that is moved.
     * @param toX    the x coordinate of the new destination.
     * @param toY    the y coordinate of the new destination.
     */
    private void undoToken(GameMap map, Player player, TileType token, int fromX, int fromY, int toX, int toY) {
        map.at(toX, toY).moveSettlement(map.at(fromX, fromY));
        player.undoToken(token);
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
    private void useTokenOracle(GameMap map, Player player, int x, int y) {
        if (!Game.canUseBasicTurn(map, player, player.getTerrainCard(), x, y))
            throw new RuntimeException("Can't use token oracle on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);

        map.at(x, y).placeSettlement(player);
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
    private void useTokenFarm(GameMap map, Player player, int x, int y) {
        /*
        if (!Game.canUseBasicTurn(map, player, TileType.GRAS, x, y))
            throw new RuntimeException("Can't use token farm on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
         */

        map.at(x, y).placeSettlement(player);
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
    private void useTokenTavern(GameMap map, Player player, int x, int y) {
        /*
        if (Game.canUseBasicTurn(map, player, x, y) || !gameMap.at(x, y).isAtEndOfAChain(gameMap, player))
            throw new RuntimeException("Can't use token tavern on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
         */
        map.at(x, y).placeSettlement(player);
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
    private void useTokenTower(GameMap map, Player player, int x, int y) {
        /*
        if (!canUseBasicTurn(player, x, y)
                || !allPossibleSettlementPlacementsOnBorderOfMap(player).contains(map.at(x, y)))
            throw new RuntimeException("Can't use token oracle on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
        */
        map.at(x, y).placeSettlement(player);
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
    private void useTokenOasis(GameMap map, Player player, int x, int y) {
        if (!Game.canUseBasicTurn(map, player, TileType.DESERT, x, y))
            throw new RuntimeException("Can't use token oasis on"
                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);

        map.at(x, y).placeSettlement(player);
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
    private void useTokenHarbor(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
        //TODO: REVISIT THIS
        if (!Game.canMoveSettlement(map, player, TileType.WATER, fromX, fromY, toX, toY))
            throw new RuntimeException("Can't use token harbor on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));
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
    private void useTokenPaddock(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
        if (!Game.canMoveSettlement(map, player, fromX, fromY, toX, toY))
            throw new RuntimeException("Can't use token paddock on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));
        player.useToken(TileType.PADDOCK);
    }

    /**
     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
     *
     * @param fromX the x coordinate of settlement to move.
     * @param fromY the y coordinate of settlement to move.
     * @param toX   the x coordinate of target tile to put settlement.
     * @param toY   the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use barn token.
     */
    private void useTokenBarn(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
        if (!Game.canMoveSettlement(map, player, player.getTerrainCard(), fromX, fromY, toX, toY))
            throw new RuntimeException("Can't use token barn on"
                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));
        player.useToken(TileType.BARN);
    }

    /**
     * set the player for the AI.
     *
     * @param aiPlayer the player to set.
     */
    public void setAiPlayer(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    /**
     * set the win conditions of the game.
     *
     * @param winConditions the win conditions.
     */
    public void setWinConditions(List<WinCondition> winConditions) {
        this.winConditions = winConditions;
    }
}


//import java.util.Set;
//// Temporary storage for methods that will be relevant for AI players later on
//public class AIGame extends Game {
//
//    /**s
//     * Throws if it's not the given player's turn.
//     *
//     * @param player the player to check if it's their turn.
//     */
//    protected void checkIsPlayersTurn(Player player) {
//        if (!isPlayersTurn(player))
//            throw new RuntimeException("It's not the player's turn!");
//    }
//
//    /**
//     * Checks if the given player can still place settlements this turn.
//     *
//     * @param player the player to check if they can still place settlements this turn.
//     */
//    protected void checkHasRemainingSettlements(Player player) {
//        if (!player.hasRemainingSettlements())
//            throw new RuntimeException("Player has no settlements left!");
//    }
//
//    /**
//     * Moves a settlement to a new position within a given terrain.
//     *
//     * @param player  the player whose turn it is and who owns the settlement.
//     * @param terrain the terrain it is getting placed.
//     * @param fromX   the old horizontal position of the settlement on the map.
//     * @param fromY   the old vertical position of the settlement on the map.
//     * @param toX     the new horizontal position of the settlement on the map.
//     * @param toY     the new vertical position of the settlement on the map.
//     */
//    public void moveSettlement(GameMap gameMap, Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {
//
//        if (!canMoveSettlement(gameMap, player, terrain, fromX, fromY, toX, toY))
//            throw new RuntimeException("can't move settlement on terrain");
//
//      /* we just read the message from the server, this is for AI later on
//        // Take token from player if settlement was last one on special place
//        map.at(fromX, fromY).surroundingSettlements(map, player).size() == 1
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//
//        // Take token from player if settlement was last one on special place
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//        */
//
//        gameMap.at(fromX, fromY).moveSettlement(gameMap.at(toX, toY));
//
//        /*
//        Tile token = map.specialPlaceInSurrounding(toX, toY);
//
//        // Player gets a token if settlement is next to special place
//        if (token != null) {
//            player.addToken(token);
//            map.at(toX, toY).takeTokenFromSpecialPlace();
//        }
//
//         */
//    }
//
//    /**
//     * Places a settlement as a basic turn and throws if the move is not valid.
//     *
//     * @param x      the horizontal position of the settlement on the map.
//     * @param y      the vertical position of the settlement on the map.
//     * @param player the player whose turn it is and who owns the settlement.
//     */
//    @Override
//    public void unsafePlaceSettlement(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, x, y))
//            throw new RuntimeException("can't place settlement");
//
//        gameMap.at(x, y).placeSettlement(player);
//        player.remainingSettlements--;
//
//        Set<Tile> specialPlaces = gameMap.at(x, y).surroundingSpecialPlaces(gameMap);
//
//        // Player gets a token if settlement is next to special place
//        for (var specialPlace : specialPlaces) {
//            player.addToken(specialPlace);
//        }
//    }
//
//    /**
//     * Moves a settlement to a new position without context of a terrain.
//     *
//     * @param player the player whose turn it is and who owns the settlement.
//     * @param fromX  the old horizontal position of the settlement on the map.
//     * @param fromY  the old vertical position of the settlement on the map.
//     * @param toX    the new horizontal position of the settlement on the map.
//     * @param toY    the new vertical position of the settlement on the map.
//     */
//    @Override
//    public void unsafeMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
//        if (!canMoveSettlement(player, fromX, fromY, toX, toY))
//            throw new RuntimeException("can't move settlement");
//
//        /* we just read the message from the server, this is for AI later on
//        // Take token from player if settlement was last one on special place
//        map.at(fromX, fromY).surroundingSettlements(map, player).size() == 1
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//         */
//
//        gameMap.at(fromX, fromY).moveSettlement(gameMap.at(toX, toY));
//
//        /*
//        Tile token = map.specialPlaceInSurrounding(toX, toY);
//
//        // Player gets a token if settlement is next to special place
//        if (token != null) {
//            player.addToken(token);
//            map.at(toX, toY).takeTokenFromSpecialPlace();
//        }
//
//         */
//    }
//
//    /**
//     * Use the oracle token. The player is allowed to place an extra settlement on a tile that has the same type as
//     * the players' terrain card.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use oracle token.
//     */
//    @Override
//    public void useTokenOracle(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, player.getTerrainCard(), x, y))
//            throw new RuntimeException("Can't use token oracle on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.ORACLE);
//    }
//
//    /**
//     * Use the farm token. The player can place an extra settlement on gras.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use farm token.
//     */
//    @Override
//    public void useTokenFarm(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, TileType.GRAS, x, y))
//            throw new RuntimeException("Can't use token farm on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.FARM);
//    }
//
//    /**
//     * Use the Tavern token. The player can place an extra settlement at the front or back of a
//     * chain of settlements that is owned by the player.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use tavern token.
//     */
//    @Override
//    public void useTokenTavern(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, x, y) || !gameMap.at(x,y).isAtEndOfAChain(gameMap, player))
//            throw new RuntimeException("Can't use token tavern on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.TAVERN);
//    }
//
//    /**
//     * Use tower token. The player can place a token at the border of the map.
//     *
//     * @param player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use tower token.
//     */
//    @Override
//    public void useTokenTower(Player player, int x, int y) {
//        /*
//        if (!canUseBasicTurn(player, x, y)
//                || !allPossibleSettlementPlacementsOnBorderOfMap(player).contains(map.at(x, y)))
//            throw new RuntimeException("Can't use token oracle on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//        */
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.TOWER);
//    }
//
//    /**
//     * Use Oasis token. The player can place an extra settlement on Desert.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use oasis token.
//     */
//    @Override
//    public void useTokenOasis(Player player, int x, int y) {
//        if (canUseBasicTurn(player, TileType.DESERT, x, y))
//            throw new RuntimeException("Can't use token oasis on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.OASIS);
//    }
//
//    /**
//     * Use Harbor token. The player can move a settlement from any tile to a water tile.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use harbor token.
//     */
//    @Override
//    public void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
//        //TODO: REVISIT THIS
//        if (canMoveSettlement(player, TileType.WATER, fromX, fromY, toX, toY))
//            throw new RuntimeException("Can't use token harbor on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY);
//        player.useToken(TileType.HARBOR);
//    }
//
//    /**
//     * Use Paddock token. The player can move a settlement two tiles in horizontal or diagonal
//     * direction.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use paddock token.
//     */
//    @Override
//    public void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
//        if (canUseBasicTurn(player, toX, toY))
//            throw new RuntimeException("Can't use token paddock on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlement(player, fromX, fromY, toX, toY);
//        player.useToken(TileType.PADDOCK);
//    }
//
//    /**
//     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use barn token.
//     */
//    @Override
//    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
//        if (!canMoveSettlement(player, player.getTerrainCard(), fromX, fromY, toX, toY))
//            throw new RuntimeException("Can't use token barn on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
//        player.useToken(TileType.BARN);
//    }
//}
