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


//TODO: DELETE BOT WHEN GAME IS  FINISHED
public class BotReducer extends Reducer<KBState> {
    public static final String CONNECT_BOT = "CONNECT_BOT";
    public static final String START_TURN_BOT = "START_TURN_BOT";

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

        //TODO: make difficulty changeable
        oldState.Bots().put(client, new AIGame(oldState.gameMap(),0 ));

        state.setBots(oldState.Bots());
        return state;
    }


    @Reduce(action = START_TURN_BOT)
    public DeferredState onStartTurnBot(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        if(client.getClientId() == oldState.nextPlayer()
                && oldState.Bots().containsKey(client)
                && oldState.Bots().get(client).aiPlayer.getRemainingSettlementsOfTurn() > 0){
            List<Tile> moves= oldState.Bots().get(client).randomPlacement(oldState.nextTerrainCard(), oldState.gameMap());

            for (Tile t : moves) {
                client.placeSettlement(t.x, t.y);
            }
            client.endTurn();
            }

        return state;
    }

}
