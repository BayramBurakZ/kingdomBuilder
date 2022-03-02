package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.actions.chat.*;
import kingdomBuilder.actions.game.*;
import kingdomBuilder.actions.general.*;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gamelogic.WinCondition;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.PlayerData;
import kingdomBuilder.redux.*;

import kingdomBuilder.generated.DeferredState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KBReducer implements Reducer<KBState> {

    @Override
    public DeferredState reduce(Store<KBState> store, KBState oldState, Action action) {
        System.out.println("Reducer Log: " + action.getClass().getSimpleName());

        /*
        https://stackoverflow.com/questions/29570767/switch-over-type-in-java
        using HashMap to accomplish the same thing at runtime that a switch statement would do at compile-time
         */

        if (action instanceof ClientAddAction a)
            return reduce(oldState, a);
        else if (action instanceof ClientRemoveAction a)
            return reduce(oldState, a);
        else if (action instanceof SetPreferredNameAction a)
            return reduce(oldState, a);
        else if (action instanceof ChatSendAction a)
            return reduce(oldState, a);
        else if (action instanceof ChatReceiveAction a)
            return reduce(oldState, a);
        else if (action instanceof ConnectAction a)
            return reduce(store, oldState, a);
        else if (action instanceof DisconnectAction a)
            return reduce(oldState, a);
        else if (action instanceof SetSceneLoaderAction a)
            return reduce(oldState, a);
        else if (action instanceof BetterColorModeAction a)
            return reduce(oldState, a);
        else if (action instanceof LoggedInAction a)
            return reduce(oldState, a);
        else if (action instanceof ApplicationExitAction a)
            return reduce(oldState, a);
        else if (action instanceof HostGameAction a)
            return reduce(oldState, a);
        else if (action instanceof GameAddAction a)
            return reduce(oldState, a);
        else if (action instanceof QuadrantAddAction a)
            return reduce(oldState, a);
        else if (action instanceof JoinGameAction a)
            return reduce(oldState, a);
        else if (action instanceof PlayerAddAction a)
            return reduce(oldState, a);
        else if (action instanceof PlayerRemoveAction a)
            return reduce(oldState, a);
        else if (action instanceof SetPlayersAction a)
            return reduce(oldState, a);
        else if (action instanceof MyGameAction a)
            return reduce(oldState, a);
        else if (action instanceof WinConditionsAction a)
            return reduce(oldState, a);
        else if (action instanceof TerrainOfTurnAction a)
            return reduce(oldState, a);
        else if (action instanceof TurnStartAction a)
            return reduce(oldState, a);
        else if (action instanceof ServerTurnAction a)
            return reduce(oldState, a);
        else if (action instanceof ClientTurnAction a)
            return reduce(store, oldState, a);
        else if (action instanceof TurnEndAction a)
            return reduce(oldState, a);
        else if (action instanceof ReadyGameAction a)
            return reduce(oldState, a);
        else if (action instanceof ReceiveTokenAction a)
            return reduce(oldState, a);
        else if( action instanceof ActivateTokenAction a)
            return reduce(oldState, a);
        else if( action instanceof ScoreAction a)
            return reduce(oldState, a);
        else if( action instanceof UploadQuadrantAction a)
            return reduce(oldState, a);
        else if( action instanceof GameEndAction a)
            return reduce(oldState, a);
        else if( action instanceof RemoveTokenAction a)
            return reduce(oldState, a);

            System.out.println("Unknown action");
        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, ClientAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.put(a.clientData.clientId(), a.clientData);
        state.setClients(clients);

        return state;
    }

    private DeferredState reduce(KBState oldState, ClientRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.remove(a.clientData.clientId());
        state.setClients(clients);

        return state;
    }

    private DeferredState reduce(KBState oldState, SetPreferredNameAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setClientPreferredName(a.clientName);
        return state;
    }

    private DeferredState reduce(KBState oldState, ChatSendAction a) {
        oldState.client().chat(a.receiverIds, a.message);
        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, ChatReceiveAction a) {
        DeferredState state = new DeferredState(oldState);

        state.setMessage(a.chatMessage);

        return state;
    }

    private DeferredState reduce(Store<KBState> store, KBState oldState, ConnectAction a) {
        DeferredState state = new DeferredState(oldState);

        Client client;
        try {
            client = oldState.selector().connect(a.address);
        } catch (IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }

        Thread selectorThread = oldState.selectorThread();
        if (selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector().isRunning();

            selectorThread = new Thread(oldState.selector());
            selectorThread.setName("SelectorThread");
            selectorThread.start();

            state.setSelectorThread(selectorThread);
        }

        client.onLoggedIn.subscribe(m -> store.dispatch(new LoggedInAction(m)));

        client.onClientsReply.subscribe(m -> {
            for (ClientData c : m.clients()) {
                store.dispatch( // TODO: use ClientData in network message
                        new ClientAddAction(new ClientData(c.clientId(), c.name(), c.gameId())));
            }
        });

        client.onGamesReply.subscribe(m -> {
            for (GameData g : m.games()) {
                store.dispatch( // TODO: use GameData in network message
                        new GameAddAction(new GameData(g.clientId(), g.gameType(), g.gameId(), g.gameName(),
                                g.gameDescription(), g.playerLimit(), g.playersJoined())));
            }
        });

        client.onQuadrantsReply.subscribe(m -> {
            for (int quadrantId : m.quadrantIds()) {
                client.quadrantRequest(quadrantId);
            }
        });

        client.onMessageReceived.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

        client.onQuadrantReply.subscribe(m -> store.dispatch(new QuadrantAddAction(m)));

        client.onClientJoined.subscribe(m -> store.dispatch(new ClientAddAction(m.clientData())));

        client.onClientLeft.subscribe(m -> store.dispatch(new ClientRemoveAction(m.clientData())));

        client.onPlayerJoined.subscribe(m -> store.dispatch(new PlayerAddAction(m)));

        client.onPlayerLeft.subscribe(m -> store.dispatch(new PlayerRemoveAction(m)));

        client.onGameHosted.subscribe(m -> store.dispatch(new GameAddAction(m.gameData())));

        client.onWelcomeToGame.subscribe(m -> {
            client.myGameRequest();
            client.playersRequest();
        });

        client.onPlayersReply.subscribe(m -> store.dispatch(new SetPlayersAction(m)));

        client.onMyGameReply.subscribe(m -> store.dispatch(new MyGameAction(m)));

        client.onWinCondition.subscribe(m -> store.dispatch(new WinConditionsAction(m)));

        client.onTurnStart.subscribe(m -> store.dispatch(new TurnStartAction(m)));

        client.onTerrainTypeOfTurn.subscribe(m -> store.dispatch(new TerrainOfTurnAction(m)));

        client.onSettlementPlaced.subscribe(m -> store.dispatch(
                new ServerTurnAction(new ServerTurn(m.clientId(), ServerTurn.TurnType.PLACE, m.row(), m.column(), -1, -1))));

        client.onSettlementRemoved.subscribe(m -> store.dispatch(
                new ServerTurnAction(new ServerTurn(m.clientId(), ServerTurn.TurnType.REMOVE, m.row(), m.column(), -1, -1))));

        client.onTokenReceived.subscribe(m -> store.dispatch(new ReceiveTokenAction(m)));

        client.onTokenLost.subscribe(m -> store.dispatch(new RemoveTokenAction(m)));

        client.onTokenUsed.subscribe(m -> {
            TileType token = TileType.valueOf(m.tokenType());
            if (token == TileType.PADDOCK || token == TileType.BARN || token == TileType.HARBOR) {
                store.dispatch(new ServerTurnAction(
                        new ServerTurn(m.clientId(), ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1)));
            }
        });

        store.subscribe(kbState -> store.dispatch(new ReadyGameAction()),
                "players", "nextTerrainCard", "nextPlayer");

        client.login(oldState.clientPreferredName());

        client.onScores.subscribe(m -> store.dispatch(new ScoreAction(m)));

        client.onQuadrantUploaded.subscribe(m ->{
            // TODO: maybe make a chat message that someone has uploaded a new quadrant
           client.quadrantRequest(m.quadrantId());
        });

        state.setClient(client);
        state.setIsConnecting(true);

        return state;
    }

    private DeferredState reduce(KBState state, ApplicationExitAction a) {
        ClientSelector selector = state.selector();
        if (selector != null && selector.isRunning()) selector.stop();

        Thread selectorThread = state.selectorThread();
        if (selectorThread != null && selectorThread.isAlive()) selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return new DeferredState(state);
    }

    private DeferredState reduce(KBState oldState, LoggedInAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setIsConnecting(false);
        state.setIsConnected(true);
        System.out.println("Is connected.");

        oldState.client().loadNamespace();
        oldState.client().clientsRequest();
        oldState.client().gamesRequest();
        oldState.client().quadrantsRequest();

        return state;
    }

    private DeferredState reduce(KBState oldState, DisconnectAction a) {
        DeferredState state = new DeferredState(oldState);

        // TODO: remove sceneloader/controller
        if (a.wasKicked) {
            var sceneLoader = oldState.sceneLoader();
            sceneLoader.getChatViewController().onYouHaveBeenKicked();
        }

        oldState.client().disconnect();
        state.setClient(null);
        state.setIsConnected(false);

        oldState.clients().clear();
        state.setClients(oldState.clients());

        oldState.games().clear();
        state.setGames(oldState.games());

        return state;
    }

    private DeferredState reduce(KBState oldState, SetSceneLoaderAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setSceneLoader(a.sceneLoader);
        return state;
    }

    private DeferredState reduce(KBState oldState, BetterColorModeAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setBetterColorsActive(a.active);
        return state;
    }

    private DeferredState reduce(KBState oldState, PlayerAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var clientData = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, clientData.name(), a.gameId));
        state.setClients(clients);

        if (a.gameId == oldState.client().getGameId()) {
            // for some reason you only get the color of a player via ?players
            oldState.client().playersRequest();
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, PlayerRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), Client.NO_ID));
        state.setClients(clients);
        return state;
    }

    private DeferredState reduce(KBState oldState, HostGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client().hostGame(a.gameName, a.gameDescription, a.playerLimit, a.timeLimit, a.turnLimit,
                a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4);
        return state;
    }

    private DeferredState reduce(KBState oldState, GameAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var games = oldState.games();
        games.put(a.gameData.gameId(), a.gameData);
        state.setGames(games);
        return state;
    }

    private DeferredState reduce(KBState oldState, QuadrantAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var quadrants = oldState.quadrants();
        quadrants.put(a.quadrantId, a.fieldTypes);
        state.setQuadrants(quadrants);
        return state;
    }

    private DeferredState reduce(KBState oldState, JoinGameAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        var clientData = clients.get(oldState.client().getClientId());
        clients.put(clientData.clientId(), new ClientData(clientData.clientId(), clientData.name(), a.gameId));
        state.setClients(clients);

        oldState.client().joinGame(a.gameId);
        state.setClient(oldState.client());
        state.setJoinedGame(true);
        return state;
    }

    private DeferredState reduce(KBState oldState, SetPlayersAction a) {
        DeferredState state = new DeferredState(oldState);

        List<PlayerData> playersList = a.playersReply.playerDataList();

        ArrayList<Player> playerGame = new ArrayList<>();
        HashMap<Integer, Player> playerMap = new HashMap<>();

        for (PlayerData pd : playersList) {
            ClientData cd = oldState.clients().get(pd.clientId());

            Player p = new Player(pd.clientId(), cd.name(), PlayerColor.valueOf(pd.color()),
                    Game.DEFAULT_STARTING_SETTLEMENTS);
            playerGame.add(p);
            playerMap.put(cd.clientId(), p);
        }

        state.setPlayers(playerGame);
        state.setPlayersMap(playerMap);
        return state;
    }

    private DeferredState reduce(KBState oldState, MyGameAction a) {
        DeferredState state = new DeferredState(oldState);

        GameMap gameMap = new GameMap(
                GameMap.DEFAULT_STARTING_TOKEN_COUNT,
                oldState.quadrants().get(a.myGameReply.boardData().quadrantId1()),
                oldState.quadrants().get(a.myGameReply.boardData().quadrantId2()),
                oldState.quadrants().get(a.myGameReply.boardData().quadrantId3()),
                oldState.quadrants().get(a.myGameReply.boardData().quadrantId4()));

        state.setMyGameReply(a.myGameReply);
        state.setGameMap(gameMap);
        return state;
    }

    private DeferredState reduce(KBState oldState, WinConditionsAction a) {
        DeferredState state = new DeferredState(oldState);

        // TODO: remove debug messages
        System.out.println(
                a.winConditionReply.winCondition1() + " " +
                        a.winConditionReply.winCondition2() + " " +
                        a.winConditionReply.winCondition3());

        ArrayList<WinCondition> wc = new ArrayList<>();
        wc.add(WinCondition.valueOf(a.winConditionReply.winCondition1()));
        wc.add(WinCondition.valueOf(a.winConditionReply.winCondition2()));
        wc.add(WinCondition.valueOf(a.winConditionReply.winCondition3()));

        state.setWinConditions(wc);

        return state;
    }

    private DeferredState reduce(KBState oldState, TerrainOfTurnAction a) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.players() != null && oldState.currentPlayer() != null) {
            oldState.currentPlayer().setTerrainCard(TileType.valueOf(a.terrainTypeOfTurn.terrainType()));
            state.setCurrentPlayer(oldState.currentPlayer());
        }

        state.setNextTerrainCard(TileType.valueOf(a.terrainTypeOfTurn.terrainType()));
        return state;
    }

    private DeferredState reduce(KBState oldState, TurnStartAction a) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.players() != null && !oldState.playersMap().isEmpty()) {

            oldState.playersMap().get(a.turnStart.clientId()).startTurn();
            state.setCurrentPlayer(oldState.playersMap().get(a.turnStart.clientId()));
        }

        state.setNextPlayer(a.turnStart.clientId());
        return state;
    }

    private DeferredState reduce(KBState oldState, ServerTurnAction a) {
        DeferredState state = new DeferredState(oldState);

        switch (a.turn.type) {

            case PLACE -> {
                ServerTurn lastTurn = oldState.gameLastTurn() instanceof ServerTurn ?
                        (ServerTurn) oldState.gameLastTurn() : null;

                if (lastTurn != null && lastTurn.type == ServerTurn.TurnType.TOKEN_USED) {
                    state.setGameLastTurn(new ServerTurn(
                            a.turn.clientId, ServerTurn.TurnType.TOKEN_USED, lastTurn.x, lastTurn.y, a.turn.x, a.turn.y));
                } else {
                    // this x,y needs to be swapped because game-logic uses swapped coordinates internally
                    Game.unsafePlaceSettlement(oldState.gameMap(), oldState.currentPlayer(), a.turn.y, a.turn.x);
                    //game.placeSettlement(game.currentPlayer, a.turn.y, a.turn.x);
                    state.setGameLastTurn(a.turn);
                }
                state.setGameMap(oldState.gameMap());
            }
            case REMOVE -> {
                ServerTurn lastTurn = oldState.gameLastTurn() instanceof ServerTurn ?
                        (ServerTurn) oldState.gameLastTurn() : null;
                if (lastTurn != null && lastTurn.type == ServerTurn.TurnType.TOKEN_USED) {
                    // this x,y needs to be swapped because game-logic uses swapped coordinates internally
                    Game.unsafeMoveSettlement(oldState.gameMap(), oldState.currentPlayer(), a.turn.y, a.turn.x, lastTurn.toY, lastTurn.toX);
                    // this x,y doesn't need to be swapped since we're treating it as an incoming server message
                    state.setGameMap(oldState.gameMap());
                    state.setGameLastTurn(new ServerTurn(
                            a.turn.clientId, ServerTurn.TurnType.MOVE, a.turn.x, a.turn.y, lastTurn.toX, lastTurn.toY));
                }
            }
            case TOKEN_USED -> {
                state.setGameLastTurn(a.turn);
            }
        }
        return state;
    }

    private DeferredState reduce(Store<KBState> store, KBState oldState, ClientTurnAction a) {
        DeferredState state = new DeferredState(oldState);

        Player player = oldState.playersMap().get(a.turn.clientId);
        int x = a.turn.x;
        int y = a.turn.y;
        int toX = a.turn.toX;
        int toY = a.turn.toY;

        switch (a.turn.type) {
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
                store.dispatch(new ServerTurnAction(
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1)));
                state.setToken(null);
            }
            case PADDOCK -> {
                Game.useTokenPaddock(player, x, y, toX, toY);
                oldState.client().useTokenPaddock(x, y, toX, toY);
                store.dispatch(new ServerTurnAction(
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1)));
                state.setToken(null);
            }
            case BARN -> {
                Game.useTokenBarn(player, x, y, toX, toY);
                oldState.client().useTokenBarn(x, y, toX, toY);
                store.dispatch(new ServerTurnAction(
                        new ServerTurn(oldState.client().getClientId(),
                                ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1)));
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

    private DeferredState reduce(KBState oldState, TurnEndAction a) {
        DeferredState state = new DeferredState(oldState);

        oldState.client().endTurn();
        //TODO: end turn in gamelogic
        return state;
    }

    private DeferredState reduce(KBState oldState, ReadyGameAction a) {
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

            state.setCurrentPlayer(currentPlayer);
            state.setGameStarted(true);
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, ReceiveTokenAction a) {
        DeferredState state = new DeferredState(oldState);

        Game.unsafeCheckForTokens(oldState.gameMap(), oldState.currentPlayer(), a.getPayload().column(), a.getPayload().row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    private DeferredState reduce(KBState oldState, RemoveTokenAction a){
        DeferredState state = new DeferredState(oldState);

        Game.unsafeRemoveToken(oldState.gameMap(), oldState.currentPlayer(), a.getPayload().column(), a.getPayload().row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    private DeferredState reduce(KBState oldState, ActivateTokenAction a){
        DeferredState state = new DeferredState(oldState);

        state.setToken(a.getToken());

        return state;
    }

    private DeferredState reduce(KBState oldState, ScoreAction a){
        DeferredState state = new DeferredState(oldState);

        state.setScores(a.scores);

        return state;
    }

    private DeferredState reduce(KBState oldState, UploadQuadrantAction a){

        oldState.client().uploadQuadrant(a.quadrant);
        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, GameEndAction a){
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

        return state;
    }
}
