package kingdomBuilder.network;

import kingdomBuilder.network.internal.MessageSocket;
import kingdomBuilder.network.protocol.server.*;
import kingdomBuilder.network.protocol.server.reply.ReplyClients;
import kingdomBuilder.network.protocol.server.request.RequestClients;
import kingdomBuilder.network.util.Event;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class Client {
    private int id;
    private String name;
    private int gameId;

    private final MessageSocket socket;

    private record Cookie(
            Class<?> expectedResponseType,
            CompletableFuture<?> future
    ) {
    }

    private final Queue<Cookie> cookieQueue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public final Event<Message> onMessage = new Event<>();
    public final Event<ClientJoined> onClientJoined = new Event<>();
    public final Event<ClientLeft> onClientLeft = new Event<>();
    public final Event<YouHaveBeenKicked> onYouHaveBeenKicked = new Event<>();
    public final Event<Void> onDisconnect = new Event<>();

    public Client(String address, int port) throws IOException {
        socket = new MessageSocket(address, port);
    }

    /**
     * Announces the client's preferred name to the server.
     * @param name The preferred name of the client.
     * @return A future holding the response of the server. The name in the response object may be changed by the server,
     * to avoid name collisions.
     */
    public CompletableFuture<WelcomeToServer> join(String name) {
        socket.sendMessage(new IAm(name));
        CompletableFuture<WelcomeToServer> fut = new CompletableFuture<>();
        var newFut = fut.thenApply( m-> {
            this.id = m.clientId();
            this.name = m.name();
            this.gameId = m.gameId();
            return m;
        });
        Cookie ck = new Cookie(WelcomeToServer.class, fut);
        cookieQueue.offer(ck);
        return newFut;
    }

    /**
     * Sends a chat message to the specified client IDs.
     * @param message The chat message to be sent.
     * @param receiver The list of the receivers' IDs.
     */
    public void chat(String message, List<Integer> receiver) {
        socket.sendMessage(new Chat(receiver.toArray(new Integer[0]), message));
    }

    /**
     * Requests a list of all the clients that are on the server.
     * @return The CompletableFuture that will contain the list of the client IDs on the server.
     */
    public CompletableFuture<ReplyClients> requestClients() {
        socket.sendMessage(new RequestClients());
        CompletableFuture<ReplyClients> fut = new CompletableFuture<>();
        Cookie ck = new Cookie(ReplyClients.class, fut);
        cookieQueue.offer(ck);
        return fut;
    }

    public void listen() {

        // Sets the isRunning flag, if it's not set yet;
        // otherwise just exits the method.
        if(!isRunning.compareAndSet(false, true))
            throw new IllegalStateException("Client::listen may be called only from one thread.");

        while (isRunning.compareAndSet(true, true)) {
            boolean receivedSomething = false;
            do {
                try {
                    receivedSomething = socket.receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (receivedSomething);

            while (socket.hasUnprocessedMessages() && !cookieQueue.isEmpty()) {
                synchronized (cookieQueue) {
                    Cookie ck = cookieQueue.peek();
                    Object resp = socket.pollMessageAs(ck.expectedResponseType());
                    if (resp != null) {
                        CompletableFuture<Object> fut = (CompletableFuture<Object>) ck.future();
                        fut.complete(resp);
                        cookieQueue.poll();
                    } else {
                        System.out.println("Warning: Cookie didn't match");
                        // TODO: if the cookie doesn't match and we have more messages, we should check our cookie
                        //   against the other messages as well. If the cookie never matches (maybe if a fitting
                        //   response doesn't arrive within a time limit), it should print an error or something.
                        break;
                    }
                }
            }

            while (socket.hasUnprocessedMessages()) {
                String msg = socket.peekMessageContents();

                // TODO:
                //   Automatically generate a semantically equivalent segment using annotation processors,
                //   once regression in Gradle 7.3 has been fixed.
                //

                if (msg.startsWith("[SERVER_MESSAGE] [MESSAGE]")) {
                    Message typedMsg = (Message) socket.pollMessageAs(Message.class);
                    onMessage.dispatch(typedMsg);
                    continue;
                }

                if(msg.startsWith("[SERVER_MESSAGE] [CLIENT_JOINED]")) {
                    ClientJoined typedMsg = (ClientJoined) socket.pollMessageAs(ClientJoined.class);
                    onClientJoined.dispatch(typedMsg);
                    continue;
                }

                if(msg.startsWith("[SERVER_MESSAGE] [CLIENT_LEFT]")) {
                    ClientLeft typedMsg = (ClientLeft) socket.pollMessageAs(ClientLeft.class);
                    onClientLeft.dispatch(typedMsg);
                    continue;
                }

                if(msg.startsWith("[SERVER_MESSAGE] [YOU_HAVE_BEEN_KICKED]")) {
                    YouHaveBeenKicked typedMsg = (YouHaveBeenKicked) socket.pollMessageAs(YouHaveBeenKicked.class);
                    onYouHaveBeenKicked.dispatch(typedMsg);
                    continue;
                }

                if(msg.startsWith("[SERVER_MESSAGE] [PING]")) {
                    socket.sendString("pong");
                    continue;
                }

                socket.skipMessage();
            }
        }

        onDisconnect.dispatch(null);
    }

    /**
     * Sends 'Bye' message to the server and closes the socket if it was still connected,
     * then stops the listening thread (client::listen).
     */
    public void disconnect() {
        isRunning.set(false);

        if (socket.isConnected()) {
            socket.sendMessage(new Bye());
            try {
                socket.closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the client is connected.
     * @return True if the client is connected, false otherwise.
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Checks if the client's listening thread is currently running.
     * @return True if the listening thread is running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning.compareAndSet(true, true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGameId() {
        return gameId;
    }
}
