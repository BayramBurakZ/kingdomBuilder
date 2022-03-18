package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.AIGame;
import kingdomBuilder.gamelogic.GameMap;
import kingdomBuilder.gamelogic.Tile;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Reduce;

import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BotReducer extends Reducer<KBState> {
    public static final String CONNECT_BOT = "CONNECT_BOT";
    public static final String START_TURN_BOT = "START_TURN_BOT";
    public static final String PLACE_BOT = "PLACE_BOT";

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
        client.onTerrainTypeOfTurn.subscribe(m -> store.dispatch(START_TURN_BOT, client));

        client.login("AI");
        client.loadNamespace();
        client.joinGame(oldState.client().getGameId());

        oldState.Bots().add(client);
        state.setBots(oldState.Bots());
        return state;
    }

    @Reduce(action = START_TURN_BOT)
    public DeferredState onStartTurnBot(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        if (client.getClientId() == oldState.nextPlayer())
            AIGame.randomPlacement(oldState.gameMap(), oldState.currentPlayer(), oldState.nextTerrainCard(), store);

        return state;
    }

    @Reduce(action = PLACE_BOT)
    public DeferredState placeBot(Store<KBState> unused, KBState oldState, List<Tile> moves) {
        DeferredState state = new DeferredState(oldState);

        Client bot = null;
        for (Client c : oldState.Bots()) {
            if (c.getClientId() == oldState.nextPlayer())
                bot = c;
        }

        if (bot == null) return state;

        for (Tile t : moves) {
            bot.placeSettlement(t.x, t.y);
        }
        bot.endTurn();

        return state;
    }
}
