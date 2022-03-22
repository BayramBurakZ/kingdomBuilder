package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.actions.chat.ChatSendAction;
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
    public static final String SET_PREFERRED_NAME = "SET_PREFERRED_NAME";
    public static final String JOIN_GAME = "JOIN_GAME";
    public static final String SET_PLAYERS = "SET_PLAYERS";
    public static final String ACTIVATE_TOKEN = "ACTIVATE_TOKEN";
    public static final String SCORE = "SCORE";
    public static final String START_TURN = "START_TURN";
    public static final String CLIENT_TURN = "CLIENT_TURN";
    public static final String SERVER_TURN = "SERVER_TURN";
    public static final String END_TURN = "END_TURN";
    public static final String GRANT_TOKEN = "GRANT_TOKEN";
    public static final String REVOKE_TOKEN = "REVOKE_TOKEN";
    public static final String READY_GAME = "READY_GAME";
    public static final String END_GAME = "END_GAME";
    public static final String MY_GAME = "MY_GAME";
    public static final String TERRAIN_OF_TURN = "TERRAIN_OF_TURN";
    public static final String BETTER_COLOR_MODE = "BETTER_COLOR_MODE";
    public static final String ADD_GAME = "ADD_GAME";
    public static final String SET_WIN_CONDITION = "SET_WIN_CONDITION";
    public static final String PLAYERS_OF_GAME = "PLAYERS_OF_GAME";

    public GameReducer() {
        registerReducers(this);
    }

    @Reduce(action = SET_PREFERRED_NAME)
    public DeferredState onSetPreferredName(Store<KBState> unused, KBState oldState, String name) {
        DeferredState state = new DeferredState(oldState);
        state.setClientPreferredName(name);
        return state;
    }

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

    @Reduce(action = "ChatSendAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, ChatSendAction a) {
        oldState.client().chat(a.receiverIds, a.message);
        return new DeferredState(oldState);
    }

    @Reduce(action = BETTER_COLOR_MODE)
    public DeferredState reduce(Store<KBState> unused, KBState oldState, Boolean activate) {
        DeferredState state = new DeferredState(oldState);
        state.setBetterColorsActive(activate);
        return state;
    }

    @Reduce(action = "PlayerAddAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, PlayerAddAction a) {
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

    @Reduce(action = "PlayerRemoveAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, PlayerRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), Client.NO_ID));
        state.setClients(clients);
        return state;
    }

    @Reduce(action = "HostGameAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, HostGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client().hostGame(a.gameName, a.gameDescription, a.playerLimit, a.timeLimit, a.turnLimit,
                a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4);
        return state;
    }

    @Reduce(action = ADD_GAME)
    public DeferredState onAddGame(Store<KBState> unused, KBState oldState, GameData gameData) {
        DeferredState state = new DeferredState(oldState);
        final var games = oldState.games();
        games.put(gameData.gameId(), gameData);
        state.setGames(games);

        oldState.client().playersOfGame(gameData.gameId());
        return state;
    }

    @Reduce(action = "QuadrantAddAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, QuadrantAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var quadrants = oldState.quadrants();
        quadrants.put(a.quadrantId, a.fieldTypes);
        state.setQuadrants(quadrants);
        return state;
    }

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

    @Reduce(action = SET_WIN_CONDITION)
    public DeferredState onSetWinConditions(Store<KBState> unused, KBState oldState, kingdomBuilder.network.protocol.WinCondition winCondition) {
        DeferredState state = new DeferredState(oldState);

        ArrayList<WinCondition> wc = new ArrayList<>(Arrays.asList(
                WinCondition.valueOf(winCondition.winCondition1()),
                WinCondition.valueOf(winCondition.winCondition2()),
                WinCondition.valueOf(winCondition.winCondition3())
        ));

        state.setWinConditions(wc);

        return state;
    }

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

    @Reduce(action = END_TURN)
    public DeferredState onEndTurn(Store<KBState> unused, KBState oldState, Void unused2) {
        DeferredState state = new DeferredState(oldState);

        oldState.client().endTurn();
        //TODO: end turn in gamelogic
        return state;
    }

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

    @Reduce(action = GRANT_TOKEN)
    public DeferredState onGrantToken(Store<KBState> unused, KBState oldState, TokenReceived payload) {
        DeferredState state = new DeferredState(oldState);

        Game.unsafeCheckForTokens(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    @Reduce(action = REVOKE_TOKEN)
    public DeferredState onRevokeToken(Store<KBState> unused, KBState oldState, TokenLost payload){
        DeferredState state = new DeferredState(oldState);

        Game.unsafeRemoveToken(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    @Reduce(action = ACTIVATE_TOKEN)
    public DeferredState onActivateToken(Store<KBState> unused, KBState oldState, TileType tileType){
        DeferredState state = new DeferredState(oldState);

        state.setToken(tileType);

        return state;
    }

    @Reduce(action = SCORE)
    public DeferredState reduce(Store<KBState> unused, KBState oldState, Scores scores){
        DeferredState state = new DeferredState(oldState);

        state.setScores(scores);

        return state;
    }

    @Reduce(action = "UploadQuadrantAction")
    public DeferredState reduce(Store<KBState> unused, KBState oldState, UploadQuadrantAction a){
        DeferredState state = new DeferredState(oldState);

        oldState.client().uploadQuadrant(a.quadrant);
        return state;
    }

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

    @Reduce(action = PLAYERS_OF_GAME)
    public DeferredState reduce(Store<KBState> unused, KBState oldState, PlayersOfGameReply reply){
        DeferredState state = new DeferredState(oldState);

        oldState.playersOfGame().put(reply.gameId(), reply.clientIds());
        state.setPlayersOfGame(oldState.playersOfGame());

        return state;
    }
}
