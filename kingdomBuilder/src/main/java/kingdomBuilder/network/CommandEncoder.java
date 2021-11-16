package kingdomBuilder.network;

import java.util.List;

public interface CommandEncoder {
    String iam(String name);
    String bye();
    String pong();
    String load(String module);
    String unload(String module);
    String join(int gameId);
    String spectate(int gameId);
    String unspectate(int gameId);

    String root(String passphrase);
    String shutdown();
    String kick(int gameId);

    String chat(List<Integer> clientIds, String message);

    String version();
    String whoAmI();

    String client(int clientId);
    String clients();
    String games();
    String playersOfGame(int gameId);
    String modules();
    String myModules();
    String detailsOfGame(int gameId);
}
