package kingdomBuilder;

import kingdomBuilder.network.Client;

import java.io.IOException;

public class Boot {

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 6666);
        var fut = client.join("Yeet42");

    }

}
