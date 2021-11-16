package kingdomBuilder.network;

import java.util.List;
import java.util.stream.Collectors;

public class CommandStringEncoder implements CommandEncoder {

    @Override
    public String iam(String name) {
        return String.format("iam %s", name);
    }

    @Override
    public String bye() {
        return "bye";
    }

    @Override
    public String pong() {
        return "pong";
    }

    @Override
    public String load(String module) {
        return String.format("load %s", module);
    }

    @Override
    public String unload(String module) {
        return String.format("unload %s", module);
    }

    @Override
    public String join(int gameId) {
        return String.format("join %d", gameId);
    }

    @Override
    public String spectate(int gameId) {
        return String.format("spectate %d", gameId);
    }

    @Override
    public String unspectate(int gameId) {
        return String.format("unspectate %d", gameId);
    }

    @Override
    public String root(String passphrase) {
        return String.format("root %s", passphrase);
    }

    @Override
    public String shutdown() {
        return "shutdown";
    }

    @Override
    public String kick(int gameId) {
        return String.format("kick %d", gameId);
    }

    @Override
    public String chat(List<Integer> clientIds, String message) {
        final String clients = clientIds
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return String.format("chat [{%s};%s]", clients, message);
    }

    @Override
    public String version() {
        return "?version";
    }

    @Override
    public String whoAmI() {
        return "?whoami";
    }

    @Override
    public String client(int clientId) {
        return String.format("?client %d", clientId);
    }

    @Override
    public String clients() {
        return "?clients";
    }

    @Override
    public String games() {
        return "?games";
    }

    @Override
    public String playersOfGame(int gameId) {
        return String.format("?playersofgame %d", gameId);
    }

    @Override
    public String modules() {
        return "?modules";
    }

    @Override
    public String myModules() {
        return "?mymodules";
    }

    @Override
    public String detailsOfGame(int gameId) {
        return String.format("?detailsofgame %d", gameId);
    }
}
