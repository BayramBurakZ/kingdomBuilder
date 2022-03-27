package kingdomBuilder.gamelogic;

import kingdomBuilder.gui.controller.BotDifficulty;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AIGame represents the AI.
 */
public class AIGame {

    /**
     * The timer used for delaying turns over time.
     */
    public Timer turnTimer = new Timer();
    /**
     * The map that is shared  between  all  players.
     */
    private GameMap gameMap;
    /**
     * The player that the  AI  controls.
     */
    public Player aiPlayer;
    /**
     * The difficulty of the AI.
     */
    private BotDifficulty difficulty;
    /**
     * The win conditions of the game. Amount of difficulty to include depends on difficulty of AI.
     */
    private ArrayList<WinCondition> winConditions;
    /**
     * The players of the game.
     */
    private ArrayList<Player> players;
    /**
     * The terrain cards that have been played.
     */
    private Map<TileType, Integer> playedTerrainCards;
    /**
     * The limit in expertAI for permutation.
     */
    int PERMUTATION_LIMIT = 50;
    /**
     * The amount of recursive calls the expertAI makes.
     */
    int SEARCH_DEPTH = 2;

    /**
     * The constructor for AIGame.
     *
     * @param gameMap    the game map that is shared between all players.
     * @param difficulty the difficulty of the AI.
     */
    public AIGame(GameMap gameMap, BotDifficulty difficulty) {
        this.gameMap = gameMap;
        this.difficulty = difficulty;
        this.playedTerrainCards = setTerrainCards();
    }

    /**
     * Selects the AI that is used for the game.
     *
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> chooseAI() {
        return switch (difficulty) {
            case EASY -> randomAI();
            case EXPERT -> expertAI();
            default -> greedyAI(null);
        };
    }

    /**
     * AI that places settlements randomly without using token. Difficulty EASY
     *
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> randomAI() {

        List<ClientTurn> moves = new ArrayList(3);
        GameMap aiGameMap = new GameMap(gameMap);

        for (int i = 0; i < aiPlayer.remainingSettlementsOfTurn; i++) {

            List<Tile> placeable = aiGameMap.getAllPlaceableTiles(aiPlayer, aiPlayer.getTerrainCard()).toList();
            int index = (int) (Math.random() * (placeable.size()));
            Tile t = placeable.get(index);

            aiGameMap.at(t.x, t.y).placeSettlement(aiPlayer);
            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, t.x, t.y, -1, -1));
        }
        return moves;
    }

    /**
     * AI that uses greedy algorithm and uses token.
     * It calculates the best next placement for a settlement within a basic turn and with every token that the AI has.
     * Difficulty NORMAL: calculates for two out of three win conditions.
     * Difficulty HARD: calculates for all win conditions.
     *
     * @param map the game map to operate on.
     * @return a list of all moves that the AI makes this turn.
     */
    public List<ClientTurn> greedyAI(GameMap map) {

        GameMap aiGameMap = (map == null) ? new GameMap(gameMap) : map;
        List<ClientTurn> moves = new ArrayList<>();

        // AI uses token before basic turn.
        int settlementsLeft = useTokenAI(aiGameMap, moves, aiPlayer.getRemainingSettlements(), false);

        // AI does basic turn.
        settlementsLeft = greedyBasicTurn(aiGameMap, moves, settlementsLeft);

        // AI uses tokens at end of turn.
        useTokenAI(aiGameMap, moves, settlementsLeft, true);
        return moves;
    }

    /**
     * Calculates the next moves for the AI by placing a settlement on all possible position and valuating it.
     *
     * @param map             the map to operate on.
     * @param moves           the moves of the AI.
     * @param settlementsLeft the amount of settlements that are left.
     * @return the amount of settlements that are left.
     */
    private int greedyBasicTurn(GameMap map, List<ClientTurn> moves, int settlementsLeft) {

        int currentScore;
        int bestScore = Game.calculateScore(map, aiPlayer, winConditions, players);
        int alternativeScore = Integer.MIN_VALUE;
        Tile bestTile = null;
        Tile alternativeTile = null;

        for (int i = 0; i < aiPlayer.remainingSettlementsOfTurn; i++) {

            if (settlementsLeft <= 0)
                break;

            Set<Tile> freeTiles = map.getAllPlaceableTiles(aiPlayer, aiPlayer.getTerrainCard())
                    .collect(Collectors.toSet());

            // collect a token if possible.
            Tile bestToken = collectBestToken(map, freeTiles);
            if (bestToken != null) {
                moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, bestToken.x, bestToken.y, -1, -1));
                map.at(bestToken.x, bestToken.y).placeSettlement(aiPlayer);
                settlementsLeft--;
                continue;
            }

            for (Tile t : freeTiles) {
                map.at(t.x, t.y).placeSettlement(aiPlayer);
                currentScore = Game.calculateScore(map, aiPlayer, winConditions, players);
                map.at(t.x, t.y).removeSettlement();

                if (currentScore >= alternativeScore) {
                    alternativeScore = currentScore;
                    alternativeTile = t;
                }

                if (currentScore >= bestScore) {
                    bestScore = currentScore;
                    bestTile = t;
                }
            }

            // sometimes first move could be negative, in that case just
            if (bestTile == null)
                bestTile = alternativeTile;


            moves.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, bestTile.x, bestTile.y, -1, -1));
            map.at(bestTile.x, bestTile.y).placeSettlement(aiPlayer);
            settlementsLeft--;

            bestTile = null;
            alternativeTile = null;
            alternativeScore = Integer.MIN_VALUE;
        }
        return settlementsLeft;
    }

    /**
     * The expertAI is a variation of the greedyAI with the difference that it looks at the best three combination
     * of basic placements. If there is more than one move with the highest score, the AI simulates the following
     * rounds and values it to choose between the contested moves.
     *
     * @return the moves for the expertAI.
     */
    private List<ClientTurn> expertAI() {

        List<ClientTurn> bestTurn = new ArrayList<>();

        expertAITurn(new GameMap(gameMap),
                bestTurn,
                playedTerrainCards,
                aiPlayer.getTerrainCard(),
                aiPlayer.getRemainingSettlements(),
                SEARCH_DEPTH,
                0);

        return bestTurn;
    }

    /**
     * Calculates the turn of the expertAI with permutations or switches to greedyAIs basic turn if too many placements
     * are possible.
     *
     * @param map             the map the AI operates on.
     * @param bestTurn        the list of moves where the best turn will be saved.
     * @param playedCards     all played cards so far in that game.
     * @param settlementsLeft amount of settlements that are left.
     * @param depth           the current depth of the search.
     * @param followingScore  the score that is calculated.
     */
    private int expertAITurn(GameMap map,
                             List<ClientTurn> bestTurn,
                             Map<TileType, Integer> playedCards,
                             TileType terrain,
                             int settlementsLeft,
                             int depth,
                             int followingScore) {


        // if it is not the first call than change the reference of the original move List.
        if (followingScore != 0)
            bestTurn = new ArrayList<>();

        if (settlementsLeft <= 0 || depth <= 0)
            return followingScore;

        Map<List<ClientTurn>, Integer> bestTurns = new HashMap<>();
        List<ClientTurn> preMoves = new ArrayList<>();
        List<ClientTurn> turn;
        boolean switchGreedy = false;
        boolean tokenAvailable = false;

        // AI uses token as pre basic turn
        settlementsLeft = useTokenAI(map, preMoves, aiPlayer.getRemainingSettlements(), false);

        // anything worse than greedy can be skipped.
        int temp = settlementsLeft;
        turn = new ArrayList<>(preMoves);
        settlementsLeft = greedyBasicTurn(map, turn, settlementsLeft);
        bestTurns.put(turn, Game.calculateScore(map, aiPlayer, winConditions, players));

        // remove all new settlements
        for (int i = temp - settlementsLeft; i > 0; i--) {
            ClientTurn t = turn.get(turn.size() - i);
            map.at(t.x, t.y).removeSettlement();
            settlementsLeft++;
        }

        Set<Tile> first = map.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());
        int score = 0;

        outerLoop:
        for (Tile t : first) {

            // switch to greedy if amount of placements is too high or AI could collect a token at first placement.
            if (first.size() > PERMUTATION_LIMIT || collectBestToken(map, first) != null) {
                switchGreedy = true;
                bestTurn.addAll(greedyAI(map));
                break;
            }

            map.at(t.x, t.y).placeSettlement(aiPlayer);
            settlementsLeft--;

            if (settlementsLeft == 0)
                score = Game.calculateScore(map, aiPlayer, winConditions, players);

            Set<Tile> second = map.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());

            for (Tile l : second) {

                if (first.size() > PERMUTATION_LIMIT) {
                    switchGreedy = true;
                    map.at(t.x, t.y).removeSettlement();
                    bestTurn.addAll(greedyAI(map));
                    break outerLoop;
                }

                map.at(l.x, l.y).placeSettlement(aiPlayer);
                settlementsLeft--;

                if (settlementsLeft == 0)
                    score = Game.calculateScore(map, aiPlayer, winConditions, players);

                Set<Tile> third = map.getAllPlaceableTiles(aiPlayer, terrain).collect(Collectors.toSet());

                for (Tile k : third) {

                    if (first.size() > PERMUTATION_LIMIT) {
                        switchGreedy = true;
                        map.at(l.x, l.y).removeSettlement();
                        bestTurn.addAll(greedyAI(map));
                        break outerLoop;
                    }

                    map.at(k.x, k.y).placeSettlement(aiPlayer);
                    settlementsLeft--;

                    if (settlementsLeft >= 0)
                        score = Game.calculateScore(map, aiPlayer, winConditions, players);

                    // prefer token collections over score
                    tokenAvailable = tokenAvailable(map, t) || tokenAvailable(map, l) || tokenAvailable(map, k);

                    if (Collections.min(bestTurns.values()) <= score || tokenAvailable) {
                        turn = new ArrayList<>(preMoves);

                        switch (settlementsLeft) {
                            case -2 -> {
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, t.x, t.y, -1, -1));
                            }
                            case -1 -> {
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, t.x, t.y, -1, -1));
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, l.x, l.y, -1, -1));
                            }
                            default -> {
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, t.x, t.y, -1, -1));
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, l.x, l.y, -1, -1));
                                turn.add(new ClientTurn(aiPlayer.ID, ClientTurn.TurnType.PLACE, k.x, k.y, -1, -1));
                            }
                        }

                        // always take token at first run no matter what score the AI gets
                        if (tokenAvailable) {
                            bestTurn.addAll(turn);
                            break outerLoop;
                        }

                        bestTurns.put(turn, score);

                        // only the best three turns
                        if (bestTurns.size() > 3)
                            bestTurns.remove(Collections.min(bestTurns.entrySet(), Map.Entry.comparingByValue()).getKey());
                    }
                    map.at(k.x, k.y).removeSettlement();
                    settlementsLeft++;
                }
                map.at(l.x, l.y).removeSettlement();
                settlementsLeft++;
            }
            map.at(t.x, t.y).removeSettlement();
            settlementsLeft++;
        }

        // stop expanding when greedy is used.
        if (switchGreedy)
            return Game.calculateScore(map, aiPlayer, winConditions, players);

        // stop expanding when token is found.
        if (tokenAvailable) {
            useTokenAI(map, bestTurn, settlementsLeft, true);
            return Game.calculateScore(map, aiPlayer, winConditions, players);
        }

        var bestTurnKeys = bestTurns.keySet().stream().toList();

        // only keep the same highest score
        int max = Collections.max(bestTurns.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
        for (var k : bestTurnKeys) {
            if (bestTurns.get(k) < max)
                bestTurn.remove(k);
        }

        // terrain prediction
        Map<TileType, Integer> tempPC1 = playedCards.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); //pass

        // does not expand if only entry
        if (bestTurnKeys.size() == 1) {
            bestTurn.addAll(bestTurnKeys.get(0));
            return followingScore;
        }

        // simulate future placements
        switch (bestTurnKeys.size()) {
            case 3:
                bestTurns.put(bestTurnKeys.get(2),
                        splitTree(map, bestTurnKeys.get(2), tempPC1, settlementsLeft, depth, followingScore));

            case 2:
                bestTurns.put(bestTurnKeys.get(1),
                        splitTree(map, bestTurnKeys.get(1), tempPC1, settlementsLeft, depth, followingScore));

            case 1:
                bestTurns.put(bestTurnKeys.get(0),
                        splitTree(map, bestTurnKeys.get(0), tempPC1, settlementsLeft, depth, followingScore));
        }

        // get the best of those moves.
        var moves = Collections.max(bestTurns.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        bestTurn.addAll(moves);
        return followingScore;
    }

    /**
     * Splits the search into three branches of possible future placements ( without considering opponent placements )
     * and returns the calculated score of all those branches combined.
     *
     * @param map             the map the AI operates on.
     * @param original        the original list of moves of that turn.
     * @param playedCards     all played cards so far in that game.
     * @param settlementsLeft amount of settlements that are left.
     * @param depth           the current depth of the search.
     * @param followingScore  the score that is calculated.
     * @return the combined score of expanded branches.
     */
    private int splitTree(GameMap map,
                          List<ClientTurn> original,
                          Map<TileType, Integer> playedCards,
                          int settlementsLeft,
                          int depth,
                          int followingScore) {

        // get turn.
        GameMap tempGM1 = new GameMap(map); //PASS each
        Map<TileType, Integer> tempPC1 = playedCards; //pass
        ArrayList<TileType> predictions = predictTerrainCard(playedCards);

        // update local map.
        int tempSL = settlementsLeft; //PASS
        for (ClientTurn clientTurn : original) {
            if (!tempGM1.at(clientTurn.x, clientTurn.y).isBlocked())
                tempGM1.at(clientTurn.x, clientTurn.y).placeSettlement(aiPlayer);
            tempSL--;
        }

        followingScore += Game.calculateScore(tempGM1, aiPlayer, winConditions, players);

        // use token at the end
        tempSL = useTokenAI(tempGM1, original, tempSL, true); //PASS

        GameMap tempGM2 = new GameMap(tempGM1);
        GameMap tempGM3 = new GameMap(tempGM1);

        Map<TileType, Integer> tempPC2 =
                playedCards.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<TileType, Integer> tempPC3 =
                playedCards.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int temp = playedTerrainCards.get(predictions.get(0));
        playedTerrainCards.put(predictions.get(0), temp + 1);

        temp = playedTerrainCards.get(predictions.get(1));
        playedTerrainCards.put(predictions.get(1), temp + 1);

        temp = playedTerrainCards.get(predictions.get(1));
        playedTerrainCards.put(predictions.get(1), temp + 1);


        followingScore += expertAITurn(tempGM1, null, tempPC1, predictions.get(0),
                tempSL, depth - 1, followingScore);

        followingScore += expertAITurn(tempGM2, null, tempPC1, predictions.get(1),
                tempSL, depth - 1, followingScore);

        followingScore += expertAITurn(tempGM3, null, tempPC1, predictions.get(2),
                tempSL, depth - 1, followingScore);

        return followingScore;
    }

    /**
     * Search for a collectable token and find the best one for current win conditions.
     *
     * @param map       the map of the AI.
     * @param freeTiles the tiles settlement can be placed.
     * @return the best token to place a settlement or null.
     */
    private Tile collectBestToken(GameMap map, Set<Tile> freeTiles) {

        Set<Tile> possibleToken = new HashSet<>();
        Set<Tile> allToken = map.getTiles().filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());

        // find out which token could be collected.
        allToken.forEach(t -> {
            if (t.hasSurroundingSettlement(map, aiPlayer))
                return;

            for (Tile l : t.surroundingTiles(map).collect(Collectors.toSet()))
                if (freeTiles.contains(l))
                    possibleToken.add(t);
        });

        Set<Tile> collectableToken = new HashSet<>();
        for (Tile t : possibleToken)
            if (t.hasTokens() && t.surroundingSettlements(map, aiPlayer).collect(Collectors.toSet()).isEmpty())
                collectableToken.add(t);

        Set<Tile> bestToken = null;

        // choose a token to take.
        if (winConditions.contains(WinCondition.LORDS) || winConditions.contains(WinCondition.FARMER)) {
            if (collectableToken.stream().anyMatch(t -> t.tileType != TileType.BARN
                    && t.tileType != TileType.HARBOR && t.tileType != TileType.PADDOCK))

                bestToken = collectableToken.stream().filter(t -> t.tileType != TileType.BARN
                        && t.tileType != TileType.HARBOR
                        && t.tileType != TileType.PADDOCK).collect(Collectors.toSet());

        } else if (winConditions.contains(WinCondition.KNIGHT) || winConditions.contains(WinCondition.EXPLORER)) {

            if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.TAVERN))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.TAVERN).collect(Collectors.toSet());
            else if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.TOWER))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.TOWER).collect(Collectors.toSet());

        } else if (winConditions.contains(WinCondition.ANCHORITE)) {

            if (collectableToken.stream().anyMatch(t -> t.tileType == TileType.PADDOCK))
                bestToken = collectableToken.stream().filter(t -> t.tileType == TileType.PADDOCK).collect(Collectors.toSet());

        } else
            bestToken = collectableToken;

        int bestScore = Game.calculateScore(map, aiPlayer, winConditions, players);
        int currentScore;
        Tile bestTile = null;

        if (bestToken != null)
            for (Tile t : bestToken)
                for (Tile l : t.surroundingTiles(map).filter(freeTiles::contains).collect(Collectors.toSet())) {
                    map.at(l.x, l.y).placeSettlement(aiPlayer);
                    currentScore = Game.calculateScore(map, aiPlayer, winConditions, players);

                    if (currentScore >= bestScore) {
                        bestScore = currentScore;
                        bestTile = l;
                    }
                    map.at(l.x, l.y).removeSettlement();
                }

        if (bestToken == null)
            return null;

        if (bestTile == null) {
            Tile alternative = bestToken.stream().findAny().orElse(null);
            if (alternative != null)
                return alternative.surroundingTiles(map).filter(freeTiles::contains).findAny().orElse(null);
        }

        return bestTile;
    }

    /**
     * Checks if next to a tile is a token place that is not collected yet.
     *
     * @param map  the map of the AI.
     * @param tile the tile where the settlement is placed.
     * @return true if token is not collected and in surrounding tile. False otherwise.
     */
    private boolean tokenAvailable(GameMap map, Tile tile) {

        Set<Tile> token = tile.surroundingTiles(map).filter
                (t -> TileType.tokenType.contains(t.tileType)).collect(Collectors.toSet());

        for (Tile t : token)
            if (t.hasTokens() && !t.hasSurroundingSettlement(map, aiPlayer))
                return true;

        return false;
    }

    /**
     * find the best token for the turn.
     *
     * @param aiGameMap       the game map of the AI.
     * @param moves           the moves of this turn.
     * @param settlementsLeft the settlements the AI has left.
     * @return the amount of settlements that are left.
     */
    private int useTokenAI(GameMap aiGameMap, List<ClientTurn> moves, int settlementsLeft, boolean postBasicPlacement) {

        outerloop:
        for (TileType t : aiPlayer.getTokens().keySet()) {
            int amountToken = aiPlayer.getTokens().get(t).getRemaining();

            for (int i = 0; i < amountToken; i++) {

                // move is allowed because it does not place a settlement
                if (settlementsLeft <= 0 && !(t == TileType.PADDOCK || t == TileType.HARBOR || t == TileType.BARN))
                    continue outerloop;     //search for more tokens that could be played

                ClientTurn move = calculateScoreToken(aiGameMap, aiPlayer, t, postBasicPlacement);

                if (move != null) {
                    moves.add(move);
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
    private ClientTurn calculateScoreToken(GameMap map, Player player, TileType token, boolean postBasicPlacement) {
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

                    // always play a token that places at the end of the turn if score does not change.
                    if ((currentScore > bestScore) || (currentScore == bestScore && postBasicPlacement)) {
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
     * Returns the terrain cards in ascending order depending on how often they have been drawn within a game.
     *
     * @param playedCards the cards that have been played.
     * @return sorted Arraylist of cards in ascending order from least drawn to most.
     */
    private ArrayList<TileType> predictTerrainCard(Map<TileType, Integer> playedCards) {
        ArrayList<TileType> prediction = new ArrayList<>();
        playedCards
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> prediction.add(x.getKey()));
        return prediction;
    }

    /**
     * Update each terrain card after one is drawn.
     *
     * @param terrain the terrain card that has been drawn.
     */
    public void updateTerrainCards(TileType terrain) {

        if (playedTerrainCards.values().stream().allMatch(t -> t == 5))
            playedTerrainCards = setTerrainCards();

        switch (terrain) {
            case GRAS -> {
                int temp = playedTerrainCards.get(TileType.GRAS);
                playedTerrainCards.put(TileType.GRAS, temp + 1);
            }
            case FLOWER -> {
                int temp = playedTerrainCards.get(TileType.FLOWER);
                playedTerrainCards.put(TileType.FLOWER, temp + 1);
            }
            case FORREST -> {
                int temp = playedTerrainCards.get(TileType.FORREST);
                playedTerrainCards.put(TileType.FORREST, temp + 1);
            }
            case CANYON -> {
                int temp = playedTerrainCards.get(TileType.CANYON);
                playedTerrainCards.put(TileType.CANYON, temp + 1);
            }
            case DESERT -> {
                int temp = playedTerrainCards.get(TileType.DESERT);
                playedTerrainCards.put(TileType.DESERT, temp + 1);
            }
        }
    }

    /**
     * Initial state of all terrain cards and the amount of time that have been drawn within a game.
     *
     * @return A map of terrain types and the amount of times it has been drawn.
     */
    private Map<TileType, Integer> setTerrainCards() {
        Map<TileType, Integer> terrainCards = new HashMap<>();
        terrainCards.put(TileType.GRAS, 0);
        terrainCards.put(TileType.FLOWER, 0);
        terrainCards.put(TileType.FORREST, 0);
        terrainCards.put(TileType.CANYON, 0);
        terrainCards.put(TileType.DESERT, 0);

        return terrainCards;
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
     * Set the win conditions for the AI. Where Normal is for the greedy AI that only considers two out of three win
     * conditions.
     *
     * @param winConditions win conditions to set.
     */
    public void setWinConditions(ArrayList<WinCondition> winConditions) {

        if (difficulty == BotDifficulty.NORMAL) {
            this.winConditions = new ArrayList<>();
            this.winConditions.add(winConditions.get(0));
            this.winConditions.add(winConditions.get(1));
        } else
            this.winConditions = winConditions;
    }

    /**
     * Set all players of the game.
     *
     * @param players the players of the game.
     */
    public void setPlayers(ArrayList<Player> players) {

        this.players = players;
    }
}