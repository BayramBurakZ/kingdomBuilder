package kingdomBuilder.network;

import java.util.List;

public class CommandStringEncoder implements CommandEncoder {

    @Override
    public String iam(String name) {
        return null;
    }

    @Override
    public String bye() {
        return null;
    }

    @Override
    public String pong() {
        return null;
    }

    @Override
    public String load(String module) {
        return null;
    }

    @Override
    public String unload(String module) {
        return null;
    }

    @Override
    public String join(int gameId) {
        return null;
    }

    @Override
    public String spectate(int gameId) {
        return null;
    }

    @Override
    public String unspectate(int gameId) {
        return null;
    }

    @Override
    public String root(String passphrase) {
        return null;
    }

    @Override
    public String shutdown() {
        return null;
    }

    @Override
    public String kick(int gameId) {
        return null;
    }

    @Override
    public String chat(List<Integer> clientIds, String message) {
        return null;
    }

    @Override
    public String version() {
        return null;
    }

    @Override
    public String whoAmI() {
        return null;
    }

    @Override
    public String client(int clientId) {
        return null;
    }

    @Override
    public String clients() {
        return null;
    }

    @Override
    public String games() {
        return null;
    }

    @Override
    public String playersOfGame(int gameId) {
        return null;
    }

    @Override
    public String modules() {
        return null;
    }

    @Override
    public String myModules() {
        return null;
    }

    @Override
    public String detailsOfGame(int gameId) {
        return null;
    }
}
