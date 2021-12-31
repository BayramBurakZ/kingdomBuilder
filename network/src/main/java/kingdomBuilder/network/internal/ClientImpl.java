package kingdomBuilder.network.internal;

import kingdomBuilder.network.generated.ProtocolSerializer;
import kingdomBuilder.network.generated.ProtocolConsumer;


import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.*;

import java.io.IOException;
import java.util.List;

public class ClientImpl extends Client implements ProtocolConsumer {
    private final IOHandler ioHandler;

    public ClientImpl(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.ioHandler.setConsumer(this);
    }

    @Override
    public void login(String preferredName) {
        final String command = ProtocolSerializer.serialize(new IAm(preferredName));
        trySendCommand(command);
    }

    @Override
    public void logout() {
        final String command = ProtocolSerializer.serialize(new Bye());
        trySendCommand(command);
    }

    @Override
    public void chat(List<Integer> recipients, String message) {
        final String command = ProtocolSerializer.serialize(new Chat(recipients, message));
        trySendCommand(command);
    }

    @Override
    public boolean isConnected() {
        return ioHandler.isConnected();
    }

    @Override
    public void disconnect() {
        ioHandler.disconnect();
    }

    @Override
    public boolean hasPendingCommands() {
        return ioHandler.hasPendingCommands();
    }

    private void trySendCommand(String command) {
        try { ioHandler.sendCommand(command); }
        catch (IOException exc) { this.onConnectionLost.dispatch(this); }
    }

    @Override
    public void onFailure(String packet) {
        System.out.println("Failed to decode the following packet:");
        System.out.print("\t");
        System.out.println(packet);
    }

    /**
     * Notifies listeners, that a client left the server.
     * @param message The client data of the client, that left the server.
     */
    @Override
    public void accept(ClientLeft message) {
        onClientLeft.dispatch(message);
    }

    /**
     * Notifies listeners, that a new message was received.
     * @param message The message and its recipients.
     */
    @Override
    public void accept(Message message) {
        onMessageReceived.dispatch(message);
    }

    /**
     * Updates the internal client data and notifies listeners of this state change.
     * @param message The client data assigned by the server.
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
     * @param message The client data of the newly joined client.
     */
    @Override
    public void accept(ClientJoined message) {
        onClientJoined.dispatch(message);
    }

    /**
     * Disconnects the client and notifies listeners that client was kicked.
     * @param message Not used.
     */
    @Override
    public void accept(YouHaveBeenKicked message) {
        this.ioHandler.disconnect();
        this.onKicked.dispatch(this);
        System.out.println("KICKED!");
    }

    /**
     * Responds with a 'PONG' to the server.
     * @param message Not used.
     */
    @Override
    public void accept(Ping message) {
        final String command = ProtocolSerializer.serialize(new Pong());
        trySendCommand(command);
    }

    @Override
    public void accept(RequestClientsResponse message) {

    }

    @Override
    public void accept(GameHosted message) {

    }

    @Override
    public void accept(YouAreRoot message) {

    }

    @Override
    public void accept(NamespaceLoaded message) {

    }

    @Override
    public void accept(NamespaceUnloaded message) {

    }

    @Override
    public void accept(YouSpectateGame message) {

    }

    @Override
    public void accept(StoppedSpectating message) {

    }

    @Override
    public void accept(YouLeftGame message) {

    }

    @Override
    public void accept(WelcomeToGame message) {

    }

    @Override
    public void accept(PlayerJoined message) {

    }

    @Override
    public void accept(PlayerLeft message) {

    }

    @Override
    public void accept(TurnEndedByServer message) {

    }

    @Override
    public void accept(VersionReply message) {

    }

    @Override
    public void accept(WhoAmIReply message) {

    }

    @Override
    public void accept(ClientReply message) {

    }

    @Override
    public void accept(GamesReply message) {

    }

    @Override
    public void accept(PlayersOfGameReply message) {

    }

    @Override
    public void accept(ModulesReply message) {

    }

    @Override
    public void accept(MyNamespacesReply message) {

    }

    @Override
    public void accept(DetailsOfGameReply message) {

    }

    @Override
    public void accept(InitStart message) {

    }

    @Override
    public void accept(WinCondition message) {

    }

    @Override
    public void accept(GameStart message) {

    }

    @Override
    public void accept(YourTerrainCard message) {

    }

    @Override
    public void accept(TurnStart message) {

    }

    @Override
    public void accept(TerrainTypeOfTurn message) {

    }

    @Override
    public void accept(SettlementPlaced message) {

    }

    @Override
    public void accept(SettlementRemoved message) {

    }

    @Override
    public void accept(TokenReceived message) {

    }

    @Override
    public void accept(TokenLost message) {

    }

    @Override
    public void accept(PlayerUsedLastSettlement message) {

    }

    @Override
    public void accept(GameOver message) {

    }

    @Override
    public void accept(Scores message) {

    }

    @Override
    public void accept(QuadrantUploaded message) {

    }

    @Override
    public void accept(QuadrantReply message) {

    }

    @Override
    public void accept(QuadrantsReply message) {

    }

    @Override
    public void accept(TimeLimitReply message) {

    }

    @Override
    public void accept(PlayerLimitReply message) {

    }

    @Override
    public void accept(PlayersReply message) {

    }

    @Override
    public void accept(TurnsReply message) {

    }

    @Override
    public void accept(TurnLimitReply message) {

    }

    @Override
    public void accept(WhoseTurnReply message) {

    }

    @Override
    public void accept(SettlementsLeftReply message) {

    }

    @Override
    public void accept(BoardReply message) {

    }

    @Override
    public void accept(WinConditionReply message) {

    }

    @Override
    public void accept(MyGameReply message) {

    }
}