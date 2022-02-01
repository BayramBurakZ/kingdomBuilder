package kingdomBuilder.network.internal;

import kingdomBuilder.network.generated.ProtocolSerializer;
import kingdomBuilder.network.generated.ProtocolConsumer;


import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.*;

import java.io.IOException;
import java.util.List;

/**
 * Implements the {@link Client} using an IOHandler.
 */
public class ClientImpl extends Client implements ProtocolConsumer {
    // TODO: doc
    private final IOHandler ioHandler;

    /**
     * Initializes the client.
     *
     * @param ioHandler a handler, which deals with asynchronous IO using javas selector mechanism.
     */
    public ClientImpl(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.ioHandler.setConsumer(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void login(String preferredName) {
        final String command = ProtocolSerializer.serialize(new IAm(preferredName));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() {
        final String command = ProtocolSerializer.serialize(new Bye());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadNamespace() {
        final String command = ProtocolSerializer.serialize(new Load("kingdom_builder"));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientsRequest() {
        final String command = ProtocolSerializer.serialize(new ClientsRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gamesRequest() {
        final String command = ProtocolSerializer.serialize(new GamesRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quadrantsRequest() {
        final String command = ProtocolSerializer.serialize(new QuadrantsRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quadrantRequest(int quadrantId) {
        final String command = ProtocolSerializer.serialize(new QuadrantRequest(quadrantId));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chat(List<Integer> recipients, String message) {
        final String command = ProtocolSerializer.serialize(new Chat(recipients, message));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void myGameRequest() {
        final String command = ProtocolSerializer.serialize(new MyGameRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hostGame(String gameName,
                         String gameDescription,
                         int playerLimit,
                         int timeLimit,
                         int turnLimit,
                         int quadrantId1,
                         int quadrantId2,
                         int quadrantId3,
                         int quadrantId4) {
        final String command = ProtocolSerializer.serialize(new HostGame(
                gameName,
                gameDescription,
                playerLimit,
                timeLimit,
                turnLimit,
                quadrantId1,
                quadrantId2,
                quadrantId3,
                quadrantId4));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinGame(int gameId) {
        final String command = ProtocolSerializer.serialize(new Join(gameId));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void boardRequest() {
        // request the quadrants right after joining a game
        final String command = ProtocolSerializer.serialize(new BoardRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playersRequest() {
        // request the quadrants right after joining a game
        final String command = ProtocolSerializer.serialize(new PlayersRequest());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return ioHandler.isConnected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        ioHandler.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeSettlement(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Place(y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTurn() {
        final String command = ProtocolSerializer.serialize(new EndTurn());
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenOracle(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Oracle(y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenFarm(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Farm(y, x));
        trySendCommand(command);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void useTokenTavern(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Tavern(y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenTower(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Tower(y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenOasis(int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Oasis(y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenHarbor(int fromX, int fromY, int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Harbor(fromY, fromX, y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenPaddock(int fromX, int fromY, int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Paddock(fromY, fromX, y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useTokenBarn(int fromX, int fromY, int x, int y) {
        //hack because server uses row/column is y/x
        final String command = ProtocolSerializer.serialize(new Barn(fromY, fromX, y, x));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadQuadrant(String quadrant) {
        final String command = ProtocolSerializer.serialize(UploadQuadrant.fromString(quadrant));
        trySendCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPendingCommands() {
        return ioHandler.hasPendingCommands();
    }

    /**
     * {@inheritDoc}
     */
    private void trySendCommand(String command) {
        try {
            ioHandler.sendCommand(command);
        } catch (IOException exc) {
            this.onConnectionLost.dispatch(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure(String packet) {
        System.out.println("Failed to decode the following packet:");
        System.out.print("\t");
        System.out.println(packet);
    }

    /**
     * Notifies listeners, that a client left the server.
     *
     * @param message the client data of the client, that left the server.
     */
    @Override
    public void accept(ClientLeft message) {
        onClientLeft.dispatch(message);
    }

    /**
     * Notifies listeners, that a new message was received.
     *
     * @param message the message and its recipients.
     */
    @Override
    public void accept(Message message) {
        onMessageReceived.dispatch(message);
    }

    /**
     * Updates the internal client data and notifies listeners of this state change.
     *
     * @param message the client data assigned by the server.
     */
    @Override
    public void accept(WelcomeToServer message) {
        name = message.name();
        clientId = message.clientId();
        gameId = message.gameId();
        onLoggedIn.dispatch(this);
    }

    /**
     * Notifies listeners that a client joined the server.
     *
     * @param message the client data of the newly joined client.
     */
    @Override
    public void accept(ClientJoined message) {
        onClientJoined.dispatch(message);
    }

    /**
     * Disconnects the client and notifies listeners that client was kicked.
     *
     * @param message not used.
     */
    @Override
    public void accept(YouHaveBeenKicked message) {
        this.ioHandler.disconnect();
        this.onKicked.dispatch(this);
        System.out.println("KICKED!");
    }

    /**
     * Responds with a 'PONG' to the server.
     *
     * @param message not used.
     */
    @Override
    public void accept(Ping message) {
        final String command = ProtocolSerializer.serialize(new Pong());
        trySendCommand(command);
    }

    // TODO: doc
    @Override
    public void accept(ClientsReply message) {
        onClientsReply.dispatch(message);
    }

    /**
     * Notifies listeners, that a new game was hosted.
     *
     * @param message the data of the new game.
     */
    @Override
    public void accept(GameHosted message) {
        onGameHosted.dispatch(message);
    }

    @Override
    public void accept(YouAreRoot message) {
        onYouAreRoot.dispatch(message);
    }

    @Override
    public void accept(NamespaceLoaded message) {
        onNamespaceLoaded.dispatch(message);
    }

    @Override
    public void accept(NamespaceUnloaded message) {
        onNamespaceUnloaded.dispatch(message);
    }

    @Override
    public void accept(YouSpectateGame message) {
        onYouSpectateGame.dispatch(message);
    }

    @Override
    public void accept(StoppedSpectating message) {
        onStoppedSpectating.dispatch(message);
    }

    @Override
    public void accept(YouLeftGame message) {
        gameId = NO_ID;
        onYouLeftGame.dispatch(message);
    }

    @Override
    public void accept(WelcomeToGame message) {
        gameId = message.gameId();
        onWelcomeToGame.dispatch(message);
    }

    @Override
    public void accept(PlayerJoined message) {
        onPlayerJoined.dispatch(message);
    }

    @Override
    public void accept(PlayerLeft message) {
        onPlayerLeft.dispatch(message);
    }

    @Override
    public void accept(TurnEndedByServer message) {
        onTurnEndedByServer.dispatch(message);
    }

    @Override
    public void accept(VersionReply message) {
        onVersionReply.dispatch(message);
    }

    @Override
    public void accept(WhoAmIReply message) {
        onWhoAmIReply.dispatch(message);
    }

    @Override
    public void accept(ClientReply message) {
        onClientReply.dispatch(message);
    }

    @Override
    public void accept(GamesReply message) {
        onGamesReply.dispatch(message);
    }

    @Override
    public void accept(PlayersOfGameReply message) {
        onPlayersOfGameReply.dispatch(message);
    }

    @Override
    public void accept(ModulesReply message) {
        onModulesReply.dispatch(message);
    }

    @Override
    public void accept(MyNamespacesReply message) {
        onMyNamespacesReply.dispatch(message);
    }

    @Override
    public void accept(DetailsOfGameReply message) {
        onDetailsOfGameReply.dispatch(message);
    }

    @Override
    public void accept(InitStart message) {
        onInitStart.dispatch(message);
    }

    @Override
    public void accept(WinCondition message) {
        onWinCondition.dispatch(message);
    }

    @Override
    public void accept(GameStart message) {
        onGameStart.dispatch(message);
    }

    @Override
    public void accept(YourTerrainCard message) {
        onYourTerrainCard.dispatch(message);
    }

    @Override
    public void accept(TurnStart message) {
        onTurnStart.dispatch(message);
    }

    @Override
    public void accept(TerrainTypeOfTurn message) {
        onTerrainTypeOfTurn.dispatch(message);
    }

    @Override
    public void accept(SettlementPlaced message) {
        onSettlementPlaced.dispatch(message);
    }

    @Override
    public void accept(SettlementRemoved message) {
        onSettlementRemoved.dispatch(message);
    }

    @Override
    public void accept(TokenReceived message) {
        onTokenReceived.dispatch(message);
    }

    @Override
    public void accept(TokenLost message) {
        onTokenLost.dispatch(message);
    }

    @Override
    public void accept(PlayerUsedLastSettlement message) {
        onPlayerUsedLastSettlement.dispatch(message);
    }

    @Override
    public void accept(GameOver message) {
        onGameOver.dispatch(message);
    }

    @Override
    public void accept(Scores message) {
        onScores.dispatch(message);
    }

    @Override
    public void accept(QuadrantUploaded message) {
        onQuadrantUploaded.dispatch(message);
    }

    @Override
    public void accept(QuadrantReply message) {
        onQuadrantReply.dispatch(message);
    }

    @Override
    public void accept(QuadrantsReply message) {
        onQuadrantsReply.dispatch(message);
    }

    @Override
    public void accept(TimeLimitReply message) {
        onTimeLimitReply.dispatch(message);
    }

    @Override
    public void accept(PlayerLimitReply message) {
        onPlayerLimitReply.dispatch(message);
    }

    @Override
    public void accept(PlayersReply message) {
        onPlayersReply.dispatch(message);
    }

    @Override
    public void accept(TurnsReply message) {
        onTurnsReply.dispatch(message);
    }

    @Override
    public void accept(TurnLimitReply message) {
        onTurnLimitReply.dispatch(message);
    }

    @Override
    public void accept(WhoseTurnReply message) {
        onWhoseTurnReply.dispatch(message);
    }

    @Override
    public void accept(SettlementsLeftReply message) {
        onSettlementsLeftReply.dispatch(message);
    }

    @Override
    public void accept(BoardReply message) {
        onBoardReply.dispatch(message);
    }

    @Override
    public void accept(WinConditionReply message) {
        onWinConditionReply.dispatch(message);
    }

    @Override
    public void accept(MyGameReply message) {
        onMyGameReply.dispatch(message);
    }
}