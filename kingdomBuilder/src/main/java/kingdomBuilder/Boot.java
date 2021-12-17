package kingdomBuilder;

import kingdomBuilder.network.internal.ClientSelectorImpl;
import kingdomBuilder.network.Client;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Boot {
    private static final InetSocketAddress address = new InetSocketAddress("localhost", 6666);

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        ClientSelectorImpl sel = new ClientSelectorImpl();

        System.out.println("Created selector and started thread.");

        try {
            Client cl = sel.connect(address);

            Thread t = new Thread(sel);
            t.start();

            System.out.println("Initiated connection.");
            cl.login("Julian");
            Thread.sleep(8000);
            cl.logout();
            Thread.sleep(2000);
            System.out.println("Stopping thread.");
            cl.disconnect();
            sel.stop();
            t.join(4000);
            if(t.isAlive()) {
                System.out.println("Still alive. Interrupting.");
                t.interrupt();
            }
        }
        catch(Exception exc) {
            exc.printStackTrace();
        }
    }
}
