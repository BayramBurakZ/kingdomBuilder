package kingdomBuilder.network;

import kingdomBuilder.network.internal.MessageSocket;
import kingdomBuilder.network.protocol.*;
import kingdomBuilder.network.util.Event;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Client {
    private static Client mainClient;

    private final MessageSocket socket;

    private record Cookie(
            Class<?> expectedResponseType,
            CompletableFuture<?> future
    ) {
    }

    private final Queue<Cookie> cookieQueue = new ConcurrentLinkedQueue<>();
    private int clientId;

    public final Event<Message> onMessage = new Event<>();
    public final Event<ClientJoined> onClientJoined = new Event<>();

    public Client(String address, int port) throws IOException {
        socket = new MessageSocket(address, port);
    }

    public static void setMain(Client client) {
        if (mainClient == null) {
            mainClient = client;
            System.out.println("Main client was set.");
        } else {
            System.out.println("Main client has already been set.");
        }
    }

    public static Client getMain() {
        if (mainClient == null) {
            System.out.println("Tried to access null main client.");
        }
        return mainClient;
    }

    public CompletableFuture<WelcomeToServer> join(String name) {
        socket.sendMessage(new IAm(name));
        CompletableFuture<WelcomeToServer> fut = new CompletableFuture<>();
        Cookie ck = new Cookie(WelcomeToServer.class, fut);
        cookieQueue.offer(ck);
        return fut;
    }

    public CompletableFuture<Message> chat(String message, List<Integer> receiver) {
        socket.sendMessage(new Chat(receiver.toArray(new Integer[0]), message));
        CompletableFuture<Message> fut = new CompletableFuture<>();
        Cookie ck = new Cookie(Message.class, fut);
        cookieQueue.offer(ck);
        return fut;
    }

    public void listen() {
        while (true) {
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
                // Make this more generic and automized.
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

                socket.skipMessage();
            }
        }
    }


}
