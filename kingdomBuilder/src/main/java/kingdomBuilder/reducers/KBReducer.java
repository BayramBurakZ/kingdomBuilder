package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.actions.chat.ChatReceiveAction;
import kingdomBuilder.actions.chat.ChatSendAction;
import kingdomBuilder.actions.game.*;
import kingdomBuilder.actions.general.ApplicationExitAction;
import kingdomBuilder.actions.general.ConnectAction;
import kingdomBuilder.actions.general.DisconnectAction;
import kingdomBuilder.actions.general.LoggedInAction;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.WinCondition;
import kingdomBuilder.gamelogic.Map;
import kingdomBuilder.gamelogic.Player;
import kingdomBuilder.gamelogic.ServerTurn;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.PlayerData;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import kingdomBuilder.generated.DeferredState;

import java.io.IOException;
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
            return reduce(oldState, a);
        else if (action instanceof TurnEndAction a)
            return reduce(oldState, a);
        else if (action instanceof ReadyGameAction a)
            return reduce(oldState, a);
        else if (action instanceof ReceiveTokenAction a)
            return reduce(oldState, a);
        else if( action instanceof ActivateToken a)
            return reduce(oldState, a);
        else if( action instanceof ScoreAction a)
            return reduce(oldState, a);
        else if( action instanceof UploadQuadrantAction a)
            return reduce(oldState, a);

            System.out.println("Unknown action");
        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, ClientAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.put(a.clientData.clientId(), a.clientData);
        state.setClients(clients);

        return state;
    }

    private DeferredState reduce(KBState oldState, ClientRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
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
        oldState.client.chat(a.receiverIds, a.message);
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
            client = oldState.selector.connect(a.address);
        } catch (IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }

        Thread selectorThread = oldState.selectorThread;
        if (selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector.isRunning();

            selectorThread = new Thread(oldState.selector);
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

        store.subscribe(kbState -> store.dispatch(new ReadyGameAction()),
                "players", "nextTerrainCard", "nextPlayer");

        client.login(oldState.clientPreferredName);

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
        ClientSelector selector = state.selector;
        if (selector != null && selector.isRunning()) selector.stop();

        Thread selectorThread = state.selectorThread;
        if (selectorThread != null && selectorThread.isAlive()) selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return new DeferredState(state);
    }

    private DeferredState reduce(KBState oldState, LoggedInAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setIsConnecting(false);
        state.setIsConnected(true);
        System.out.println("Is connected.");

        oldState.client.loadNamespace();
        oldState.client.clientsRequest();
        oldState.client.gamesRequest();
        oldState.client.quadrantsRequest();

        return state;
    }

    private DeferredState reduce(KBState oldState, DisconnectAction a) {
        DeferredState state = new DeferredState(oldState);

        // TODO: remove sceneloader/controller
        if (a.wasKicked) {
            var sceneLoader = oldState.sceneLoader;
            sceneLoader.getChatViewController().onYouHaveBeenKicked();
        }

        oldState.client.disconnect();
        state.setClient(null);
        state.setIsConnected(false);

        oldState.clients.clear();
        state.setClients(oldState.clients);

        oldState.games.clear();
        state.setGames(oldState.games);

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
        final var clients = oldState.clients;
        var clientData = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, clientData.name(), a.gameId));
        state.setClients(clients);

        if (a.gameId == oldState.client.getGameId()) {
            // for some reason you only get the color of a player via ?players
            oldState.client.playersRequest();
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, PlayerRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), Client.NO_ID));
        state.setClients(clients);
        return state;
    }

    private DeferredState reduce(KBState oldState, HostGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client.hostGame(a.gameName, a.gameDescription, a.playerLimit, a.timeLimit, a.turnLimit,
                a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4);
        return state;
    }

    private DeferredState reduce(KBState oldState, GameAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var games = oldState.games;
        games.put(a.gameData.gameId(), a.gameData);
        state.setGames(games);
        return state;
    }

    private DeferredState reduce(KBState oldState, QuadrantAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var quadrants = oldState.quadrants;
        quadrants.put(a.quadrantId, a.fieldTypes);
        state.setQuadrants(quadrants);
        return state;
    }

    private DeferredState reduce(KBState oldState, JoinGameAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        var clientData = clients.get(oldState.client.getClientId());
        clients.put(clientData.clientId(), new ClientData(clientData.clientId(), clientData.name(), a.gameId));
        state.setClients(clients);

        oldState.client.joinGame(a.gameId);
        Game game = new Game();
        state.setClient(oldState.client);
        state.setGame(game);
        return state;
    }

    private DeferredState reduce(KBState oldState, SetPlayersAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        List<PlayerData> playersList = a.playersReply.playerDataList();

        Player[] player = new Player[playersList.size()];

        int i = 0;
        for (PlayerData pd : playersList) {
            ClientData cd = oldState.clients.get(pd.clientId());

            // TODO: remove constant
            player[i++] = new Player(pd.clientId(), cd.name(), Game.PlayerColor.valueOf(pd.color()), 40);
        }

        game.setPlayers(player);

        state.setGame(game);
        state.setPlayers(List.of(player));
        return state;
    }

    private DeferredState reduce(KBState oldState, MyGameAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        Map map = new Map(
                Map.DEFAULT_STARTING_TOKEN_COUNT,
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId1()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId2()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId3()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId4()));

        game.setMap(map);
        game.setGameInfo(a.myGameReply);

        state.setGame(game);
        return state;
    }

    private DeferredState reduce(KBState oldState, WinConditionsAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        // TODO: remove debug messages
        System.out.println(
                a.winConditionReply.winCondition1() + " " +
                        a.winConditionReply.winCondition2() + " " +
                        a.winConditionReply.winCondition3());

        WinCondition[] winConditions = {
                WinCondition.valueOf(a.winConditionReply.winCondition1()),
                WinCondition.valueOf(a.winConditionReply.winCondition2()),
                WinCondition.valueOf(a.winConditionReply.winCondition3())
        };

        game.setWinConditions(winConditions);

        state.setGame(game);

        return state;
    }

    private DeferredState reduce(KBState oldState, TerrainOfTurnAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        if (oldState.players != null) {
            game.startTurn(oldState.nextPlayer, Game.TileType.valueOf(a.terrainTypeOfTurn.terrainType()));
            state.setGame(game);
        }

        state.setNextTerrainCard(Game.TileType.valueOf(a.terrainTypeOfTurn.terrainType()));

        return state;
    }

    private DeferredState reduce(KBState oldState, TurnStartAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        state.setNextPlayer(a.turnStart.clientId());
        return state;
    }

    private DeferredState reduce(KBState oldState, ServerTurnAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        switch (a.turn.type) {

            case PLACE -> {
                ServerTurn lastTurn = oldState.gameLastTurn instanceof ServerTurn ?
                        (ServerTurn) oldState.gameLastTurn : null;
                if (lastTurn != null && lastTurn.type == ServerTurn.TurnType.REMOVE) {
                    game.moveSettlement(game.currentPlayer, lastTurn.x, lastTurn.y, a.turn.x, a.turn.y);
                    state.setGameLastTurn(new ServerTurn(
                            a.turn.clientId, ServerTurn.TurnType.MOVE, lastTurn.x, lastTurn.y, a.turn.x, a.turn.y));
                } else {
                    game.placeSettlementOnTerrain(game.currentPlayer, game.currentPlayer.getTerrainCard(), a.turn.y, a.turn.x);
                    //game.placeSettlement(game.currentPlayer, a.turn.y, a.turn.x);
                    state.setGameLastTurn(a.turn);
                    state.setGame(game);
                }
            }
            case REMOVE -> {
                state.setGameLastTurn(a.turn);
            }
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, ClientTurnAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;

        Player player = game.playersMap.get(a.turn.clientId);
        int x = a.turn.x;
        int y = a.turn.y;
        int toX = a.turn.toX;
        int toY = a.turn.toY;
        // TODO: use enums from gamelogic I guess
        switch (a.turn.type) {

            case PLACE -> {
                oldState.client.placeSettlement(x, y);
                //oldState.client.placeSettlement();
            }

            case ORACLE -> {
                game.useToken(player, Game.TileType.ORACLE, x, y, toX, toY);
                //oldState.client.useTokenOracle();
            }
            case FARM -> {
                game.useToken(player, Game.TileType.FARM, x, y, toX, toY);
                //oldState.client.useTokenFarm();
            }
            case TAVERN -> {
                game.useToken(player, Game.TileType.TAVERN, x, y, toX, toY);
                //oldState.client.useTokenTavern();
            }
            case TOWER -> {
                game.useToken(player, Game.TileType.TOWER, x, y, toX, toY);
                //oldState.client.useTokenTower();
            }
            case HARBOR -> {
                game.useToken(player, Game.TileType.HARBOR, x, y, toX, toY);
                //oldState.client.useTokenHarbor();
            }
            case PADDOCK -> {
                game.useToken(player, Game.TileType.PADDOCK, x, y, toX, toY);
                //oldState.client.useTokenPaddock();
            }
            case BARN -> {
                game.useToken(player, Game.TileType.BARN, x, y, toX, toY);
                //oldState.client.useTokenBarn();
            }
            case OASIS -> {
                game.useToken(player, Game.TileType.OASIS, x, y, toX, toY);
                //oldState.client.useTokenOasis();
            }
        }

        return state;
    }

    private DeferredState reduce(KBState oldState, TurnEndAction a) {
        DeferredState state = new DeferredState(oldState);

        oldState.client.endTurn();
        //TODO: end turn in gamelogic
        return state;
    }

    private DeferredState reduce(KBState oldState, ReadyGameAction a) {
        DeferredState state = new DeferredState(oldState);
        final var game = oldState.game;
        // super mario hack
        if (oldState.gameStarted == false && oldState.players != null &&
                oldState.game.getMyGameReply().playerLimit() == oldState.players.size()
                && oldState.nextPlayer >= 0 && oldState.nextTerrainCard != null) {
            game.startTurn(oldState.nextPlayer, oldState.nextTerrainCard);
            state.setGameStarted(true);
            state.setGame(game);
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, ReceiveTokenAction a) {
        DeferredState state = new DeferredState(oldState);

        state.setGame(oldState.game);

        return state;
    }

    private DeferredState reduce(KBState oldState, ActivateToken a){
        DeferredState state = new DeferredState(oldState);

        final Game game = oldState.game;

        if (a.getToken() != null) {
            game.selectedToken = a.getToken();
            state.setGame(game);
        }

        state.setToken(a.getToken());

        return state;
    }

    private DeferredState reduce(KBState oldState, ScoreAction a){
        DeferredState state = new DeferredState(oldState);

        state.setScores(a.scores);

        return state;
    }

    private DeferredState reduce(KBState oldState, UploadQuadrantAction a){

        oldState.client.uploadQuadrant(a.quadrant);
        return new DeferredState(oldState);
    }
}
