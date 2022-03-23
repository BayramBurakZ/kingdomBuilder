package kingdomBuilder.gamelogic;

import kingdomBuilder.KBState;
import kingdomBuilder.gui.controller.BotDifficulty;
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
     * the difficulty of the AI.
     */
    private BotDifficulty difficulty;

    /**
     * The win conditions of the game. Amount of difficulty to include depends on difficulty of AI.
     */
    private ArrayList<WinCondition> winConditions;

    /**
     * the players of the game.
     */
    private ArrayList<Player> players;

    /**
     * the constructor for AIGame.
     *
     * @param gameMap    the game map that is shared between all players.
     * @param difficulty the difficulty of the AI.
     */
    public AIGame(GameMap gameMap, BotDifficulty difficulty) {
        this.gameMap = gameMap;
        this.difficulty = difficulty;
    }

    /**
     * selects the AI that is used for the game.
     *
     * @param terrain of the current turn.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> chooseAI(TileType terrain) {
        return switch (difficulty) {
            case EASY -> randomPlacement(terrain);
            default -> greedyPlacement(terrain);
        };
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
     * It calculates the best next placement for a settlement within a basic turn and with every token  that the  AI  has.
     *
     * @param terrain of the current turn.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> greedyPlacement(TileType terrain) {

        GameMap aiGameMap = new GameMap(gameMap);
        List<ClientTurn> moves = new ArrayList<>();
        boolean firstMoveOnSpecialPlace = false;
        int settlementsLeft = aiPlayer.getRemainingSettlements();

        settlementsLeft = findBestTurnToken(aiGameMap, moves, settlementsLeft);

        Set<Tile> freeTiles = aiGameMap.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());
        Tile bestToken = findBestToken(aiGameMap, freeTiles);

        if (bestToken != null) {
            firstMoveOnSpecialPlace = true;
            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, bestToken.x, bestToken.y, -1, -1));
            System.out.println("BOT: Placing Basic Settlement next to Token on: x" + bestToken.x + ", y: " + bestToken.y);
            aiGameMap.at(bestToken.x, bestToken.y).placeSettlement(aiPlayer);
            settlementsLeft--;
        }


        // make basic turn
        int currentScore = 0;
        int bestScore = Game.calculateScore(aiGameMap, aiPlayer, winConditions, players);
        Tile bestTile = null;

        int alternativeScore = Integer.MIN_VALUE;
        Tile alternativeTile = null;

        for (int i = 0; i < aiPlayer.remainingSettlementsOfTurn; i++) {
            if (firstMoveOnSpecialPlace || settlementsLeft <= 0) {
                firstMoveOnSpecialPlace = false;
                continue;
            }

            freeTiles = aiGameMap.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());

            for (Tile t : freeTiles) {
                aiGameMap.at(t.x, t.y).placeSettlement(aiPlayer);
                currentScore = Game.calculateScore(aiGameMap, aiPlayer, winConditions, players);
                aiGameMap.at(t.x, t.y).removeSettlement();

                if (currentScore >= alternativeScore) {
                    alternativeScore = currentScore;
                    alternativeTile = t;
                }

                if (currentScore >= bestScore) {
                    bestScore = currentScore;
                    bestTile = t;
                }
            }
            if (bestTile == null) {
                // sometimes first move could be negative, in that case just
                bestTile = alternativeTile;
            }

            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, bestTile.x, bestTile.y, -1, -1));
            System.out.println("BOT: Placing Basic Settlement on: x" + bestTile.x + ", y: " + bestTile.y);
            aiGameMap.at(bestTile.x, bestTile.y).placeSettlement(aiPlayer);
            settlementsLeft--;

            bestTile = null;
            alternativeTile = null;
            alternativeScore = Integer.MIN_VALUE;
        }

        // use tokens
        findBestTurnToken(aiGameMap, moves, settlementsLeft);

        return moves;
    }


    /**
     * Search for a collectable token and find the best one for current win conditions.
     *
     * @param map       the map of the AI.
     * @param freeTiles the tiles settlement can be placed.
     * @return the best token to place a settlement or null.
     */
    private Tile findBestToken(GameMap map, Set<Tile> freeTiles) {

        Set<Tile> token = map.getTiles().filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());
        Set<Tile> collectableToken = new HashSet<>();

        // prefer a special place as first turn
        token.forEach(t -> {
            if(t.hasSurroundingSettlement(map, aiPlayer))
                return;

            for (Tile l : t.surroundingTiles(map).collect(Collectors.toSet()))
                    if (freeTiles.contains(l))
                        collectableToken.add(t);
        });

        if (collectableToken.isEmpty())
            return null;

        Tile bestToken = null;

        if (winConditions.contains(WinCondition.LORDS) || winConditions.contains(WinCondition.FARMER)) {
            if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.ORACLE))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.ORACLE).findAny().orElse(null);
            else if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.FARM))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.FARM).findAny().orElse(null);
            else if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.OASIS))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.OASIS).findAny().orElse(null);

        } else if (winConditions.contains(WinCondition.KNIGHT) || winConditions.contains(WinCondition.EXPLORER)) {
            if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.TAVERN))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.TAVERN).findAny().orElse(null);
            else if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.TOWER))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.TOWER).findAny().orElse(null);

        } else if (winConditions.contains(WinCondition.ANCHORITE)) {
            if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.PADDOCK))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.PADDOCK).findAny().orElse(null);

        } else
            bestToken = token.stream().findAny().orElse(null);

        if (bestToken != null)
            for (Tile t : bestToken.surroundingTiles(map).collect(Collectors.toSet()))
                if (freeTiles.contains(t)) return t;

        return null;
    }

    /**
     * find the best token for the turn.
     *
     * @param aiGameMap       the game map of the AI.
     * @param moves           the moves of this turn.
     * @param settlementsLeft the settlements the AI has left.
     * @return the amount of settlements that are left.
     */
    private int findBestTurnToken(GameMap aiGameMap, List<ClientTurn> moves, int settlementsLeft) {
        // use tokens
        outerloop2:
        for (TileType t : aiPlayer.getTokens().keySet()) {

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
        return settlementsLeft;
    }

    /**
     * Calculates all possible placements of a settlement with a token and return the move with the highest score.
     *
     * @param map    the map  for  the  AI.
     * @param player the player that  the  AI  controls .
     * @param token  the token that is used.
     * @return the  best  move for  a  settlement with that token.
     */
    private ClientTurn calculateScoreToken(GameMap map, Player player, TileType token) {
        ClientTurn bestMove = null;

        Set<Tile> freeTiles;
        int currentScore = 0;
        int bestScore = Game.calculateScore(map, player, winConditions, players);

        switch (token) {

            case HARBOR -> {
                Set<Tile> settlements =
                        Game.allTokenHarborTiles(map, player, false).collect(Collectors.toSet());

                for (Tile t : settlements) {
                    freeTiles = Game.allTokenHarborTiles(map, player, true).collect(Collectors.toSet());

                    if (lastSettlementOnToken(map, player, t))
                        continue;

                    outerloop1:
                    for (Tile l : freeTiles) {
                        Set<Tile> surroundingToken = l.surroundingTiles(map).filter
                                (n -> TileType.tokenType.contains(n.tileType)).collect(Collectors.toSet());

                        for (Tile k : surroundingToken) {
                            if (!k.hasSurroundingSettlement(map, player)) {
                                bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                                break outerloop1;
                            }
                        }

                        useTokenHarbor(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, players);

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

                    if (lastSettlementOnToken(map, player, t))
                        continue;

                    outerloop2:
                    for (Tile l : freeTiles) {
                        Set<Tile> surroundingToken = l.surroundingTiles(map).filter
                                (n -> TileType.tokenType.contains(n.tileType)).collect(Collectors.toSet());

                        for (Tile k : surroundingToken) {
                            if (!k.hasSurroundingSettlement(map, player)) {
                                bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                                break outerloop2;
                            }
                        }

                        useTokenPaddock(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, players);

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

                    if (lastSettlementOnToken(map, player, t))
                        continue;

                    outerloop3:
                    for (Tile l : freeTiles) {
                        Set<Tile> surroundingToken = l.surroundingTiles(map).filter
                                (n -> TileType.tokenType.contains(n.tileType)).collect(Collectors.toSet());

                        for (Tile k : surroundingToken) {
                            if (!k.hasSurroundingSettlement(map, player)) {
                                bestMove = tokenToClientTurn(player, token, t.x, t.y, l.x, l.y);
                                break outerloop3;
                            }
                        }

                        useTokenBarn(map, player, t.x, t.y, l.x, l.y);
                        currentScore = Game.calculateScore(map, player, winConditions, players);

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

                outerloop4:
                for (Tile l : freeTiles) {
                    Set<Tile> surroundingToken = l.surroundingTiles(map).filter
                            (n -> TileType.tokenType.contains(n.tileType)).collect(Collectors.toSet());

                    for (Tile k : surroundingToken) {
                        if (!k.hasSurroundingSettlement(map, player)) {
                            bestTile = l;
                            break outerloop4;
                        }
                    }

                    useToken(map, player, token, l.x, l.y);
                    currentScore = Game.calculateScore(map, player, winConditions, players);

                    // always play a token
                    if (currentScore >= bestScore) {
                        bestScore = currentScore;
                        bestTile = l;
                    }
                    undoToken(map, player, token, l.x, l.y);
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
     */
    private void useTokenOracle(GameMap map, Player player, int x, int y) {
        map.at(x, y).placeSettlement(player);
        player.useToken(TileType.ORACLE);
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenFarm(GameMap map, Player player, int x, int y) {
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
     */
    private void useTokenTavern(GameMap map, Player player, int x, int y) {
        map.at(x, y).placeSettlement(player);
        player.useToken(TileType.TAVERN);
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenTower(GameMap map, Player player, int x, int y) {
        map.at(x, y).placeSettlement(player);
        player.useToken(TileType.TOWER);
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     */
    private void useTokenOasis(GameMap map, Player player, int x, int y) {
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
     */
    private void useTokenHarbor(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
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
     */
    private void useTokenPaddock(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
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
     */
    private void useTokenBarn(GameMap map, Player player, int fromX, int fromY, int toX, int toY) {
        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));
        player.useToken(TileType.BARN);
    }

    /**
     * check if the settlement on tile is the last one next to a token.
     *
     * @param map    the map of the AI.
     * @param player the player controlled by AI.
     * @param tile   the tile where the settlement is placed.
     * @return true if settlement is the last one next to token. False otherwise.
     */
    private boolean lastSettlementOnToken(GameMap map, Player player, Tile tile) {

        boolean lastSettlement = false;
        Set<Tile> token = tile.surroundingTiles(map).filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());

        for (Tile t : token) {
            if (t.surroundingSettlements(map, player).collect(Collectors.toSet()).size() == 1)
                lastSettlement = true;
        }
        return lastSettlement;
    }

    /**
     * Checks if next to a tile is a token place that is not collected yet.
     *
     * @param map    the map of the AI.
     * @param player the player controlled by AI.
     * @param tile   the tile where the settlement is placed.
     * @return true if token is not collected and in surrounding tile. False otherwise.
     */
    private boolean freeTokenOnSurrounding(GameMap map, Player player, Tile tile) {

        boolean freeToken = false;
        Set<Tile> token = tile.surroundingTiles(map).filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());

        for (Tile t : token) {
            if (t.surroundingSettlements(map, player).collect(Collectors.toSet()).size() == 0)
                freeToken = true;
        }
        return freeToken;
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
     * set the win conditions for the AI.
     *
     * @param winConditions win conditions to set.
     */
    public void setWinConditions(ArrayList<WinCondition> winConditions) {
        this.winConditions = winConditions;
    }

    /**
     * set all players of the game.
     *
     * @param players the players of the game.
     */
    public void setPlayers(ArrayList<Player> players) {

        this.players = players;
    }
}