package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.TokenReceived;
import kingdomBuilder.redux.Reduce;

import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.util.List;


//TODO: DELETE BOT WHEN GAME IS  FINISHED
public class BotReducer extends Reducer<KBState> {

    public static final String CONNECT_BOT = "CONNECT_BOT";
    public static final String MAKE_TURN_BOT = "MAKE_TURN_BOT";
    public static final String GRANT_TOKEN_BOT = "GRANT_TOKEN_BOT";
    public static final String ACTIVATE_TOKEN_BOT = "ACTIVATE_TOKEN_BOT";
    public static final String DISCONNECT_BOT = "DISCONNECT_BOT";

    /**
     * Constructs a new BotReducer and registers himself.
     */
    public BotReducer() {
        registerReducers(this);
    }

    @Reduce(action = CONNECT_BOT)
    public DeferredState Reduce(Store<KBState> store, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);

        Client client;
        try {
            client = oldState.selector().connect(oldState.serverAddress());
        } catch (IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }

        //message subscribes:
        /*
        - Start Turn
        - Terrain Card
        - Token received
        - Game Over (self destruct)
        */
        //client.onYourTerrainCard.subscribe(m -> store.dispatch(MAKE_TURN_BOT, client));
        store.subscribe(kbState -> store.dispatch(MAKE_TURN_BOT, client), "nextTerrainCard");
        client.onGameOver.subscribe(m -> store.dispatch(DISCONNECT_BOT, client));
        client.onTokenReceived.subscribe(m -> store.dispatch(GRANT_TOKEN_BOT, m));


        client.login("AI");
        client.loadNamespace();
        client.joinGame(oldState.client().getGameId());

        //TODO: make difficulty changeable
        oldState.Bots().put(client, new AIGame(oldState.gameMap(), 1));

        state.setBots(oldState.Bots());
        return state;
    }

    @Reduce(action = MAKE_TURN_BOT)
    public DeferredState onMakeTurnBot(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        if (oldState.currentPlayer() != null
                && client.getClientId() == oldState.nextPlayer()
                && oldState.Bots().containsKey(client)
                && oldState.Bots().get(client).aiPlayer.getRemainingSettlementsOfTurn() > 0) {

            //System.out.println(oldState.nextPlayer() + " || " + client.getClientId());
            List<ClientTurn> moves = oldState.Bots().get(client).chooseAI(oldState.nextTerrainCard());

            for (ClientTurn c : moves) {
                int x = c.x;
                int y = c.y;
                int toX = c.toX;
                int toY = c.toY;

                switch (c.type) {
                    case PLACE -> client.placeSettlement(x, y);
                    case ORACLE -> client.useTokenOracle(x, y);
                    case FARM -> client.useTokenFarm(x, y);
                    case TAVERN -> client.useTokenTavern(x, y);
                    case TOWER -> client.useTokenTower(x, y);
                    case HARBOR -> client.useTokenHarbor(x, y, toX, toY);
                    case PADDOCK -> client.useTokenPaddock(x, y, toX, toY);
                    case BARN -> client.useTokenBarn(x, y, toX, toY);
                    case OASIS -> client.useTokenOasis(x, y);
                }
            }
            client.endTurn();
        }

        return state;
    }

    @Reduce(action = GRANT_TOKEN_BOT)
    public DeferredState onGrantTokenBot(Store<KBState> unused, KBState oldState, TokenReceived payload) {
        DeferredState state = new DeferredState(oldState);

        Game.unsafeCheckForTokens(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    @Reduce(action = ACTIVATE_TOKEN_BOT)
    public DeferredState onActivateTokenBot(Store<KBState> unused, KBState oldState, TileType tileType) {
        DeferredState state = new DeferredState(oldState);

        state.setToken(tileType);

        return state;
    }

    @Reduce(action = DISCONNECT_BOT)
    public DeferredState onDisconnectBOT(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        client.onGameOver.unsubscribe(m -> store.dispatch(DISCONNECT_BOT, client));
        client.onTerrainTypeOfTurn.unsubscribe(m -> store.dispatch(MAKE_TURN_BOT, client));

        client.disconnect();
        oldState.Bots().remove(client);

        state.setBots(oldState.Bots());
        return state;
    }

}
