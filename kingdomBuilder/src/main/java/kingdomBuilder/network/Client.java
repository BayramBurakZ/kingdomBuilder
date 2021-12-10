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


public class Client {
    private int id;
    private String name;
    private int gameId;

    private final MessageSocket socket;

    private boolean running;

    private record Cookie(
            Class<?> expectedResponseType,
            CompletableFuture<?> future
    ) {
    }

    private final Queue<Cookie> cookieQueue = new ConcurrentLinkedQueue<>();

    public final Event<Message> onMessage = new Event<>();
    public final Event<ClientJoined> onClientJoined = new Event<>();
    public final Event<ClientLeft> onClientLeft = new Event<>();

    public Client(String address, int port) throws IOException {
        socket = new MessageSocket(address, port);
    }

    /**
     * Sends the 'IAm' message to the server and internally sets the client's ID, name and gameID once the
     * response arrives. Start a client::listen thread to allow the client to listen for the response.
     * @param name The preferred name of the client, if the name is already in use, it will append 'the n-th'.
     * @return The CompletableFuture that carriers the WelcomeToServer message.
     * Use future.get() to wait until the response has arrived after a client::listen thread has been started.
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

    public void chat(String message, List<Integer> receiver) {
        socket.sendMessage(new Chat(receiver.toArray(new Integer[0]), message));
    }

    public CompletableFuture<ReplyClients> requestClients() {
        socket.sendMessage(new RequestClients());
        CompletableFuture<ReplyClients> fut = new CompletableFuture<>();
        Cookie ck = new Cookie(ReplyClients.class, fut);
        cookieQueue.offer(ck);
        return fut;
    }

    public void listen() {
        running = true;
        while (running) {
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
                    } else
                        break;
                }
            }

            while (socket.hasUnprocessedMessages()) {
                String msg = socket.peekMessageContents();

                // TODO:
                //  Make this more generic and automized.
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

                socket.skipMessage();
            }
        }
    }

    /**
     * Sends 'Bye' message to the server, closes the socket and unsubscribes all listeners from events.
     */
    public void disconnect() {
        socket.sendMessage(new Bye());
        try {
            socket.closeSocket();
        }catch (IOException e){
            e.printStackTrace();
        }
        running = false;
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
