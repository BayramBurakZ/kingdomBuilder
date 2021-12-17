package kingdomBuilder.networkOutdated.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

public class MessageSocket {
    private final MessageFormatParser parser = new MessageFormatParser();
    private final MessageFormatRenderer renderer = new MessageFormatRenderer();
    private final Queue<String> messages = new ArrayDeque<>();

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public MessageSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    public <T> void sendMessage(T msg) {
        out.println(renderer.render(msg));
        out.flush();
    }

    public void sendString(String msg) {
        out.println(msg);
        out.flush();
    }

    public boolean receive() throws IOException {
        if (in.ready()) {
            String line = in.readLine();
            messages.add(line);
            return true;
        }
        return false;
    }

    public synchronized boolean hasUnprocessedMessages() {
        return !messages.isEmpty();
    }

    public synchronized String peekMessageContents() {
        return messages.peek();
    }

    public synchronized <T> T pollMessageAs(Class<T> cls) {
        String msg = messages.peek();
        T obj = parser.parseTo(msg, cls);
        if(obj != null) messages.poll();
        return obj;
    }

    public synchronized void skipMessage() {
        messages.poll();
    }

    // created by Gui-team
    public void closeSocket() throws IOException{
        if (socket.isConnected()) {
            socket.close();
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
