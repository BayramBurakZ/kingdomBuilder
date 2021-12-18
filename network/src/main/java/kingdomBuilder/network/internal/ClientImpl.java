package kingdomBuilder.network.internal;

import kingdomBuilder.network.generated.ProtocolSerializer;
import kingdomBuilder.network.generated.ProtocolConsumer;


import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.*;

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
        ioHandler.sendCommand(command);
    }

    @Override
    public void logout() {
        final String command = ProtocolSerializer.serialize(new Bye());
        ioHandler.sendCommand(command);
    }

    @Override
    public void chat(List<Integer> recipients, String message) {
        final String command = ProtocolSerializer.serialize(new Chat(recipients, message));
        ioHandler.sendCommand(command);
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
        ioHandler.sendCommand(command);
    }
}