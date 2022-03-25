package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.actions.game.*;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.gamelogic.TileType;

import kingdomBuilder.gamelogic.WinCondition;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.*;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import kingdomBuilder.generated.DeferredState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameReducer extends Reducer<KBState> {
    /**
     * Represents the String to identify the related {@link GameReducer#onSetPreferredName reduce} methode.
     */
    public static final String SET_PREFERRED_NAME = "SET_PREFERRED_NAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onJoinGame reduce} methode.
     */
    public static final String JOIN_GAME = "JOIN_GAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onSetPlayers reduce} methode.
     */
    public static final String SET_PLAYERS = "SET_PLAYERS";
    /**
     * Represents the String to identify the related {@link GameReducer#onActivateToken reduce} methode.
     */
    public static final String ACTIVATE_TOKEN = "ACTIVATE_TOKEN";
    /**
     * Represents the String to identify the related {@link GameReducer#onScore reduce} methode.
     */
    public static final String SCORE = "SCORE";
    /**
     * Represents the String to identify the related {@link GameReducer#onStartTurn reduce} methode.
     */
    public static final String START_TURN = "START_TURN";
    /**
     * Represents the String to identify the related {@link GameReducer#onClientTurn reduce} methode.
     */
    public static final String CLIENT_TURN = "CLIENT_TURN";
    /**
     * Represents the String to identify the related {@link GameReducer#onServerTurn reduce} methode.
     */
    public static final String SERVER_TURN = "SERVER_TURN";
    /**
     * Represents the String to identify the related {@link GameReducer#onEndTurn reduce} methode.
     */
    public static final String END_TURN = "END_TURN";
    /**
     * Represents the String to identify the related {@link GameReducer#onGrantToken reduce} methode.
     */
    public static final String GRANT_TOKEN = "GRANT_TOKEN";
    /**
     * Represents the String to identify the related {@link GameReducer#onRevokeToken reduce} methode.
     */
    public static final String REVOKE_TOKEN = "REVOKE_TOKEN";
    /**
     * Represents the String to identify the related {@link GameReducer#onReadyGame reduce} methode.
     */
    public static final String READY_GAME = "READY_GAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onEndGame reduce} methode.
     */
    public static final String END_GAME = "END_GAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onMyGame reduce} methode.
     */
    public static final String MY_GAME = "MY_GAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onTerrainOfTurn reduce} methode.
     */
    public static final String TERRAIN_OF_TURN = "TERRAIN_OF_TURN";
    /**
     * Represents the String to identify the related {@link GameReducer#onBetterColors reduce} methode.
     */
    public static final String BETTER_COLOR_MODE = "BETTER_COLOR_MODE";
    /**
     * Represents the String to identify the related {@link GameReducer#onAddGame reduce} methode.
     */
    public static final String ADD_GAME = "ADD_GAME";
    /**
     * Represents the String to identify the related {@link GameReducer#onSetWinConditions reduce} methode.
     */
    public static final String SET_WIN_CONDITION = "SET_WIN_CONDITION";
    /**
     * Represents the String to identify the related {@link GameReducer#onPlayersOfGame reduce} methode.
     */
    public static final String PLAYERS_OF_GAME = "PLAYERS_OF_GAME";

    /**
     * Constructs a new GameReducer and registers himself.
     * @see Reducer#registerReducers
     */
    public GameReducer() {
        registerReducers(this);
    }

    /**
     * Represents the reducer to set the preferred name of the user.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param name the name that the user sets.
     *
     * @return the deferredState.
     */
    @Reduce(action = SET_PREFERRED_NAME)
    public DeferredState onSetPreferredName(Store<KBState> unused, KBState oldState, String name) {
        DeferredState state = new DeferredState(oldState);
        state.setClientPreferredName(name);
        return state;
    }

    /**
     * Represents the reducer to handle that the client tries join a game.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param gameId the ID of the game the client tires to join.
     *
     * @return the deferredState.
     */
    @Reduce(action = JOIN_GAME)
    public DeferredState onJoinGame(Store<KBState> unused, KBState oldState, Integer gameId) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var clientData = clients.get(oldState.client().getClientId());
        clients.put(clientData.clientId(), new ClientData(clientData.clientId(), clientData.name(), gameId));
        state.setClients(clients);

        oldState.client().joinGame(gameId);
        state.setClient(oldState.client());
        state.setJoinedGame(true);
        return state;
    }

    /**
     * Represents the reducer to set the information that better colors should be shown.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param activate whether better colors should be used.
     *
     * @return the deferredState.
     */
    @Reduce(action = BETTER_COLOR_MODE)
    public DeferredState onBetterColors(Store<KBState> unused, KBState oldState, Boolean activate) {
        DeferredState state = new DeferredState(oldState);
        state.setBetterColorsActive(activate);
        return state;
    }

    /**
     * Represents the reducer to handle whenever a player joins a game.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param a the information about the client who joined game.
     *
     * @return the deferredState.
     */
    @Reduce(action = "PlayerAddAction")
    public DeferredState onAddPlayer(Store<KBState> unused, KBState oldState, PlayerAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var clientData = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, clientData.name(), a.gameId));
        state.setClients(clients);

        if (a.gameId == oldState.client().getGameId()) {
            // for some reason you only get the color of a player via ?players
            oldState.client().playersRequest();
        }
        oldState.client().gamesRequest();
        oldState.client().playersOfGame(a.gameId);

        return state;
    }

    /**
     * Represents the reducer to handle whenever a player left a game.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param a the information about the client who left a game.
     *
     * @return the deferredState.
     */
    @Reduce(action = "PlayerRemoveAction")
    public DeferredState onRemovePlayer(Store<KBState> unused, KBState oldState, PlayerRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), Client.NO_ID));
        state.setClients(clients);
        return state;
    }

    /**
     * Represents the reducer to handle whenever the client wants to host a new game.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param a the information about the game to create.
     *
     * @return the deferredState.
     */
    @Reduce(action = "HostGameAction")
    public DeferredState onHostGame(Store<KBState> unused, KBState oldState, HostGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client().hostGame(a.gameName, a.gameDescription, a.playerLimit, a.timeLimit, a.turnLimit,
                a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4);
        return state;
    }

    /**
     * Represents the reducer to handle whenever a new game is added on the server side.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param gameData all information about the new game.
     *
     * @return the deferredState.
     */
    @Reduce(action = ADD_GAME)
    public DeferredState onAddGame(Store<KBState> unused, KBState oldState, GameData gameData) {
        DeferredState state = new DeferredState(oldState);
        final var games = oldState.games();
        games.put(gameData.gameId(), gameData);
        state.setGames(games);

        oldState.client().playersOfGame(gameData.gameId());
        return state;
    }

    /**
     * Represents the reducer to handle whenever a quadrant is added to the server.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param a the information about the layout of the quadrant.
     *
     * @return the deferredState.
     */
    @Reduce(action = "QuadrantAddAction")
    public DeferredState onAddQuadrant(Store<KBState> unused, KBState oldState, QuadrantAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var quadrants = oldState.quadrants();
        quadrants.put(a.quadrantId, a.fieldTypes);
        state.setQuadrants(quadrants);
        return state;
    }

    /**
     * Represents the reducer to handle whenever the {@link PlayersReply ?players reply} is coming back.
     * Afterwards it sets the information about the players of the current game.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the information about other players in the game.
     *
     * @return the deferredState.
     */
    @Reduce(action = SET_PLAYERS)
    public DeferredState onSetPlayers(Store<KBState> unused, KBState oldState, PlayersReply payload) {
        DeferredState state = new DeferredState(oldState);

        List<PlayerData> playersList = payload.playerDataList();

        ArrayList<Player> playerGame = new ArrayList<>();
        HashMap<Integer, Player> playerMap = new HashMap<>();

        for (PlayerData pd : playersList) {
            ClientData cd = oldState.clients().get(pd.clientId());

            Player p = new Player(pd.clientId(), cd.name(), PlayerColor.valueOf(pd.color()),
                    Game.DEFAULT_STARTING_SETTLEMENTS);
            playerGame.add(p);
            playerMap.put(cd.clientId(), p);

            for( Client client : oldState.Bots().keySet()){
                if(client.getClientId() == pd.clientId())
                    oldState.Bots().get(client).setAiPlayer(p);
            }
        }

        state.setPlayers(playerGame);
        state.setPlayersMap(playerMap);
        return state;
    }

    /**
     * Represents the reducer to handle the {@link MyGameReply ?myGame reply}.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the information about the current game.
     *
     * @return the deferredState.
     */
    @Reduce(action = MY_GAME)
    public DeferredState onMyGame(Store<KBState> unused, KBState oldState, MyGameReply payload) {
        DeferredState state = new DeferredState(oldState);

        GameMap gameMap = new GameMap(
                GameMap.DEFAULT_STARTING_TOKEN_COUNT,
                oldState.quadrants().get(payload.boardData().quadrantId1()),
                oldState.quadrants().get(payload.boardData().quadrantId2()),
                oldState.quadrants().get(payload.boardData().quadrantId3()),
                oldState.quadrants().get(payload.boardData().quadrantId4()));

        state.setMyGameReply(payload);
        state.setGameMap(gameMap);
        return state;
    }

    /**
     * Represents the reducer to handle the {@link WinConditionsRequest reply to ?winconditions}
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param winCondition the information about the win conditions of the current game.
     *
     * @return the deferredState.
     */
    @Reduce(action = SET_WIN_CONDITION)
    public DeferredState onSetWinConditions(Store<KBState> unused, KBState oldState,
                                            kingdomBuilder.network.protocol.WinCondition winCondition) {
        DeferredState state = new DeferredState(oldState);

        ArrayList<WinCondition> wc = new ArrayList<>(Arrays.asList(
                WinCondition.valueOf(winCondition.winCondition1()),
                WinCondition.valueOf(winCondition.winCondition2()),
                WinCondition.valueOf(winCondition.winCondition3())
        ));

        state.setWinConditions(wc);

        return state;
    }

    /**
     * Represents the reducer to handle whenever the {@link TerrainTypeOfTurn terrain card} is chosen.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param turn the information about the current terrain card.
     *
     * @return the deferredState.
     */
    @Reduce(action = TERRAIN_OF_TURN)
    public DeferredState onTerrainOfTurn(Store<KBState> unused, KBState oldState, TerrainTypeOfTurn turn) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.players() != null && oldState.currentPlayer() != null) {
            oldState.currentPlayer().setTerrainCard(TileType.valueOf(turn.terrainType()));
            state.setCurrentPlayer(oldState.currentPlayer());
        }

        state.setNextTerrainCard(TileType.valueOf(turn.terrainType()));
        return state;
    }

    /**
     * Represents the reducer to handle whenever the {@link TurnStart turn} starts.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param turnStart the information about the client whose turn it is.
     *
     * @return the deferredState.
     */
    @Reduce(action = START_TURN)
    public DeferredState onStartTurn(Store<KBState> unused, KBState oldState, TurnStart turnStart) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.players() != null && !oldState.playersMap().isEmpty()) {

            oldState.playersMap().get(turnStart.clientId()).startTurn();
            state.setCurrentPlayer(oldState.playersMap().get(turnStart.clientId()));
        }

        state.setNextPlayer(turnStart.clientId());
        return state;
    }

    /**
     * Represents the reducer to handle whenever the server sends the client a turn.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param turn the information about the turn the server has sent.
     *
     * @return the deferredState.
     */
    @Reduce(action = SERVER_TURN)
    public DeferredState onServerTurn(Store<KBState> unused, KBState oldState, ServerTurn turn) {
        DeferredState state = new DeferredState(oldState);

        switch (turn.type) {

            case PLACE -> {
                ServerTurn lastTurn = oldState.gameLastTurn() instanceof ServerTurn ?
                        (ServerTurn) oldState.gameLastTurn() : null;

                if (lastTurn != null && lastTurn.type == ServerTurn.TurnType.TOKEN_USED) {
                    state.setGameLastTurn(new ServerTurn(
                            turn.clientId, ServerTurn.TurnType.TOKEN_USED, lastTurn.x, lastTurn.y, turn.x, turn.y));
                } else {
                    // this x,y needs to be swapped because game-logic uses swapped coordinates internally
                    Game.unsafePlaceSettlement(oldState.gameMap(), oldState.currentPlayer(), turn.y, turn.x);
                    //game.placeSettlement(game.currentPlayer, a.turn.y, a.turn.x);
                    state.setGameLastTurn(turn);
                }
                state.setGameMap(oldState.gameMap());
            }
            case REMOVE -> {
                ServerTurn lastTurn = oldState.gameLastTurn() instanceof ServerTurn ?
                        (ServerTurn) oldState.gameLastTurn() : null;
                if (lastTurn != null && lastTurn.type == ServerTurn.TurnType.TOKEN_USED) {
                    // this x,y needs to be swapped because game-logic uses swapped coordinates internally
                    Game.unsafeMoveSettlement(oldState.gameMap(), oldState.currentPlayer(), turn.y, turn.x, lastTurn.toY, lastTurn.toX);
                    // this x,y doesn't need to be swapped since we're treating it as an incoming server message
                    state.setGameMap(oldState.gameMap());
                    state.setGameLastTurn(new ServerTurn(
                            turn.clientId, ServerTurn.TurnType.MOVE, turn.x, turn.y, lastTurn.toX, lastTurn.toY));
                }
            }
            case TOKEN_USED -> {
                state.setGameLastTurn(turn);
            }
        }
        return state;
    }

    /**
     * Represents the reducer to handle when the client made a turn.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param clientTurn the information about the turn that the client made.
     *
     * @return the deferredState.
     */
    @Reduce(action = CLIENT_TURN)
    public DeferredState onClientTurn(Store<KBState> store, KBState oldState, ClientTurn clientTurn) {
        DeferredState state = new DeferredState(oldState);

        Player player = oldState.playersMap().get(clientTurn.clientId);
        int x = clientTurn.x;
        int y = clientTurn.y;
        int toX = clientTurn.toX;
        int toY = clientTurn.toY;

        switch (clientTurn.type) {
            case PLACE -> {
                Game.useBasicTurn(oldState.gameMap(), player, x, y);
                oldState.client().placeSettlement(x, y);
            }

            case ORACLE -> {
                Game.useTokenOracle(player, x, y);
                oldState.client().useTokenOracle(x, y);
                state.setToken(null);
            }
            case FARM -> {
                Game.useTokenFarm(player, x, y);
                oldState.client().useTokenFarm(x, y);
                state.setToken(null);
            }
            case TAVERN -> {
                Game.useTokenTavern(player, x, y);
                oldState.client().useTokenTavern(x, y);
                state.setToken(null);
            }
            case TOWER -> {
                Game.useTokenTower(player, x, y);
                oldState.client().useTokenTower(x, y);
                state.setToken(null);
            }
            case HARBOR -> {
                Game.useTokenHarbor(player, x, y, toX, toY);
                oldState.client().useTokenHarbor(x,y, toX, toY);
                store.dispatch(SERVER_TURN,
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1));
                state.setToken(null);
            }
            case PADDOCK -> {
                Game.useTokenPaddock(player, x, y, toX, toY);
                oldState.client().useTokenPaddock(x, y, toX, toY);
                store.dispatch(SERVER_TURN,
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1));
                state.setToken(null);
            }
            case BARN -> {
                Game.useTokenBarn(player, x, y, toX, toY);
                oldState.client().useTokenBarn(x, y, toX, toY);
                store.dispatch(SERVER_TURN,
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1));
                state.setToken(null);
            }
            case OASIS -> {
                Game.useTokenOasis(player, x, y);
                oldState.client().useTokenOasis(x, y);
                state.setToken(null);
            }
        }

        state.setGameMap(oldState.gameMap());
        return state;
    }

    /**
     * Represents the reducer to handle whenever the client ends the turn.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an unused object.
     *
     * @return the deferredState.
     */
    @Reduce(action = END_TURN)
    public DeferredState onEndTurn(Store<KBState> unused, KBState oldState, Void unused2) {
        DeferredState state = new DeferredState(oldState);

        oldState.client().endTurn();
        return state;
    }

    /**
     * Represents the reducer to handle whenever a game starts while the client need to wait for remaining replies.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an unused object.
     *
     * @return the deferredState.
     */
    @Reduce(action = READY_GAME)
    public DeferredState onReadyGame(Store<KBState> unused, KBState oldState, Void unused2) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.myGameReply() == null) {
            return state;
        }

        if (oldState.gameStarted() == false && oldState.players() != null &&
                oldState.myGameReply().playerLimit() == oldState.players().size()
                && oldState.nextPlayer() >= 0 && oldState.nextTerrainCard() != null) {

            Player currentPlayer = oldState.playersMap().get(oldState.nextPlayer());

            currentPlayer.setTerrainCard(oldState.nextTerrainCard());
            currentPlayer.startTurn();

            state.setNextTerrainCard(oldState.nextTerrainCard());
            state.setNextPlayer(currentPlayer.ID);
            state.setCurrentPlayer(currentPlayer);
            state.setGameStarted(true);

        }
        return state;
    }

    /**
     * Represents the reducer to handle whenever a player get a token and the server send the
     * {@link TokenReceived token received} message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the information about the client who received a token from an origin tile.
     *
     * @return the deferredState.
     */
    @Reduce(action = GRANT_TOKEN)
    public DeferredState onGrantToken(Store<KBState> unused, KBState oldState, TokenReceived payload) {
        DeferredState state = new DeferredState(oldState);

        Game.unsafeCheckForTokens(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    /**
     * Represents the reducer to handle whenever a player lost a token and the server send the
     * {@link TokenLost token lost} message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the information about the client who lost a token and the information about the origin
     *                of the token.
     *
     * @return the deferredState.
     */
    @Reduce(action = REVOKE_TOKEN)
    public DeferredState onRevokeToken(Store<KBState> unused, KBState oldState, TokenLost payload){
        DeferredState state = new DeferredState(oldState);

        Game.unsafeRemoveToken(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    /**
     * Represents the reducer to handle whenever the client want to use a token.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param tileType the type of token the client wants to use.
     *
     * @return the deferredState.
     */
    @Reduce(action = ACTIVATE_TOKEN)
    public DeferredState onActivateToken(Store<KBState> unused, KBState oldState, TileType tileType){
        DeferredState state = new DeferredState(oldState);

        state.setToken(tileType);

        return state;
    }

    /**
     * Represents the reducer to handle whenever the server sends the {@link Scores score} message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param scores the message that contains the score of the finished game.
     *
     * @return the deferredState.
     */
    @Reduce(action = SCORE)
    public DeferredState onScore(Store<KBState> unused, KBState oldState, Scores scores){
        DeferredState state = new DeferredState(oldState);

        state.setScores(scores);

        return state;
    }

    /**
     * Represents the reducer to handle whenever the client wants to upload a quadrant that was created in the
     * leveleditor.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param a the information about the types of all tiles of this quadrant.
     *
     * @return the deferredState.
     */
    @Reduce(action = "UploadQuadrantAction")
    public DeferredState onUploadQuadrant(Store<KBState> unused, KBState oldState, UploadQuadrantAction a){
        DeferredState state = new DeferredState(oldState);

        oldState.client().uploadQuadrant(a.quadrant);
        return state;
    }

    /**
     * Represents the reducer to handle whenever the current game end.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an unused object.
     *
     * @return the deferredState.
     */
    @Reduce(action = END_GAME)
    public DeferredState onEndGame(Store<KBState> unused, KBState oldState, Void unused2){
        DeferredState state = new DeferredState(oldState);

        state.setPlayers(null);
        state.setScores(null);
        state.setToken(null);
        state.setGameLastTurn(null);
        state.setNextTerrainCard(null);
        state.setNextPlayer(-1);
        state.setGameStarted(false);
        state.setGameMap(null);
        state.setMyGameReply(null);
        state.setPlayersMap(null);
        state.setCurrentPlayer(null);
        state.setJoinedGame(false);
        state.setWinConditions(new ArrayList<>());

        oldState.client().clientsRequest();

        return state;
    }

    /**
     * Represents the reducer to handle whenever the server sends the
     * {@link PlayersOfGameReply reply to ?playersofgame}.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param reply the information about the players of a specific game.
     *
     * @return the deferredState.
     */
    @Reduce(action = PLAYERS_OF_GAME)
    public DeferredState onPlayersOfGame(Store<KBState> unused, KBState oldState, PlayersOfGameReply reply){
        DeferredState state = new DeferredState(oldState);

        oldState.playersOfGame().put(reply.gameId(), reply.clientIds());
        state.setPlayersOfGame(oldState.playersOfGame());

        return state;
    }
}
