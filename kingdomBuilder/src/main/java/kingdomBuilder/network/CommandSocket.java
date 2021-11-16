package kingdomBuilder.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandSocket implements CommandSink {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public CommandSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String sendCommandString(String command) throws IOException {
        out.println(command);
        return in.readLine();
    }
}
