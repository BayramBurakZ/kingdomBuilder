package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.gui.controller.BotDifficulty;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.TokenReceived;
import kingdomBuilder.redux.Reduce;

import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.TimerTask;


public class BotReducer extends Reducer<KBState> {

    /**
     * Represents the String to identify the related {@link BotReducer#onConnectBot reduce} method.
     */
    public static final String CONNECT_BOT = "CONNECT_BOT";
    /**
     * Represents the String to identify the related {@link BotReducer#onMakeTurnBot reduce} method.
     */
    public static final String MAKE_TURN_BOT = "MAKE_TURN_BOT";
    /**
     * Represents the String to identify the related {@link BotReducer#onGrantTokenBot reduce} method.
     */
    public static final String GRANT_TOKEN_BOT = "GRANT_TOKEN_BOT";
    /**
     * Represents the String to identify the related {@link BotReducer#onActivateTokenBot reduce} method.
     */
    public static final String ACTIVATE_TOKEN_BOT = "ACTIVATE_TOKEN_BOT";
    /**
     * Represents the String to identify the related {@link BotReducer#onDisconnectBot reduce} method.
     */
    public static final String DISCONNECT_BOT = "DISCONNECT_BOT";
    /**
     * Represents the String to identify the related {@link BotReducer#onSetWinConditionBot reduce} method.
     */
    public static final String SET_WIN_CONDITION_BOT = "SET_WIN_CONDITION_BOT";

    /**
     * Constructs a new BotReducer and lets it register itself.
     */
    public BotReducer() {
        registerReducers(this);
    }

    /**
     * Utility method for subscribing the bots to the relevant attributes of the state.
     * @param store the store.
     */
    public static void subscribeMethods(Store<KBState> store) {
        store.subscribe(kbState -> kbState.Bots().keySet().forEach(
                c -> store.dispatch(MAKE_TURN_BOT, c)), "nextTerrainCard", "gameStarted");

        store.subscribe(kbState -> {
            if (kbState.winConditions() != null && !kbState.winConditions().isEmpty())
                kbState.Bots().keySet().forEach(c -> store.dispatch(SET_WIN_CONDITION_BOT, c));
        },"winConditions");
    }

    /**
     * Represents the reducer to handle connecting a new bot client to the server the main client is currently connected to.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param difficulty the difficulty setting of the new bot.
     *
     * @return the deferredState.
     */
    @Reduce(action = CONNECT_BOT)
    public DeferredState onConnectBot(Store<KBState> store, KBState oldState, BotDifficulty difficulty) {
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
        - Game Over (self destruct)
        */
        //client.onYourTerrainCard.subscribe(m -> store.dispatch(MAKE_TURN_BOT, client));
        /* don't subscribe every client individually, just subscribe once and iterate through the Bots() map
        store.subscribe(kbState -> store.dispatch(MAKE_TURN_BOT, client), "nextTerrainCard");
        store.subscribe(kbState -> {
            if(kbState.winConditions() != null && !kbState.winConditions().isEmpty())
                store.dispatch(SET_WIN_CONDITION_BOT, client);
            },"winConditions");

        store.subscribe(kbState ->{
                if(kbState.players() != null && !kbState.players().isEmpty())
                    store.dispatch(SET_PLAYERS_BOT, client);}
                , "players");
         */

        client.onGameOver.subscribe(m -> store.dispatch(DISCONNECT_BOT, client));
        client.onKicked.subscribe(c -> {
            if (c == client) {
                store.dispatch(DISCONNECT_BOT, client);
            }
        });
        //client.onTokenReceived.subscribe(m -> store.dispatch(GRANT_TOKEN_BOT, m));

        client.login(difficulty + " AI");
        client.loadNamespace();
        client.joinGame(oldState.client().getGameId());

        oldState.Bots().put(client, new AIGame(oldState.gameMap(), difficulty));

        state.setBots(oldState.Bots());
        return state;
    }

    /**
     * Represents the reducer to handle the next turn of a bot.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param client the bot client whose turn it is.
     *
     * @return the deferredState.
     */
    @Reduce(action = MAKE_TURN_BOT)
    public DeferredState onMakeTurnBot(Store<KBState> unused, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        AIGame aiGame = oldState.Bots().get(client);

        if (aiGame == null || !oldState.gameStarted())
            return state;

        if(oldState.nextTerrainCard() != null)
            aiGame.updateTerrainCards(oldState.nextTerrainCard());

        if (oldState.currentPlayer() != null
                && client.getClientId() == oldState.nextPlayer()
                && aiGame.aiPlayer.getRemainingSettlementsOfTurn() > 0) {

            //System.out.println(oldState.nextPlayer() + " || " + client.getClientId());
            List<ClientTurn> moves = oldState.Bots().get(client).chooseAI();

            // adding turns in reverse on the stack, so pop() gets the turns in order
            Stack<ClientTurn> stack = new Stack<>();
            for (int i = moves.size()-1; i >= 0; i--) {
                stack.push(moves.get(i));
            }

            // timer is now depends on the animation timer
            aiGame.turnTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (stack.empty()) {
                        //cancels the timer task when all moves are made and sends the end turn message
                        client.endTurn();
                        cancel();
                        return;
                    }
                    ClientTurn c = stack.pop();
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
            }, (int) (0.5 * SceneLoader.ANIMATION_TIME), (int) (1.1 * SceneLoader.ANIMATION_TIME));
        }

        return state;
    }

    /**
     * Represents the reducer to handle granting a token to a bot.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the data object containing the token information and the receiving client's ID.
     *
     * @return the deferredState.
     */
    @Reduce(action = GRANT_TOKEN_BOT)
    public DeferredState onGrantTokenBot(Store<KBState> unused, KBState oldState, TokenReceived payload) {
        DeferredState state = new DeferredState(oldState);

        Game.unsafeCheckForTokens(oldState.gameMap(), oldState.currentPlayer(), payload.column(), payload.row());

        state.setGameMap(oldState.gameMap());
        return state;
    }

    /**
     * Represents the reducer to handle activating a bot's token.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param tileType the type of token to be used by the bot.
     *
     * @return the deferredState.
     */
    @Reduce(action = ACTIVATE_TOKEN_BOT)
    public DeferredState onActivateTokenBot(Store<KBState> unused, KBState oldState, TileType tileType) {
        DeferredState state = new DeferredState(oldState);

        state.setToken(tileType);

        return state;
    }

    /**
     * Represents the reducer to handle disconnecting a bot from the server they're currently connected to.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param client the bot client to be disconnected.
     *
     * @return the deferredState.
     */
    @Reduce(action = DISCONNECT_BOT)
    public DeferredState onDisconnectBot(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        AIGame aiGame = oldState.Bots().get(client);
        if (aiGame != null && aiGame.turnTimer != null) {
            aiGame.turnTimer.cancel();
            aiGame.turnTimer = null;
        }

        client.disconnect();
        oldState.Bots().remove(client);

        state.setBots(oldState.Bots());
        return state;
    }

    /**
     * Represents the reducer to handle setting the win conditions relevant to a bot.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param client the bot client whose win conditions to set.
     *
     * @return the deferredState.
     */
    @Reduce(action = SET_WIN_CONDITION_BOT)
    public DeferredState onSetWinConditionBot(Store<KBState> store, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        if(oldState.Bots() != null)
            oldState.Bots().get(client).setWinConditions(oldState.winConditions());

        return state;
    }
}
