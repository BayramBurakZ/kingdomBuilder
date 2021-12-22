package kingdomBuilder.network;

import kingdomBuilder.network.generated.ProtocolConsumer;
import kingdomBuilder.network.protocol.*;

public class ProtocolTestConsumer implements ProtocolConsumer {
    private Object object = null;
    private boolean hasError = false;

    @Override
    public void onFailure(String packet) {
        hasError = true;
    }

    @Override
    public void accept(ClientLeft message) {
        object = message;
    }

    @Override
    public void accept(WelcomeToServer message) {
        object = message;
    }

    @Override
    public void accept(Message message) {
        object = message;
    }

    @Override
    public void accept(ClientJoined message) {
        object = message;
    }

    @Override
    public void accept(YouHaveBeenKicked message) {
        object = message;
    }

    @Override
    public void accept(Ping message) {
        object = message;
    }

    @Override
    public void accept(RequestClientsResponse message) {
        object = message;
    }

    @Override
    public void accept(GameHosted message) {
        object = message;
    }

    @Override
    public void accept(YouAreRoot message) {
        object = message;
    }

    @Override
    public void accept(NamespaceLoaded message) {
        object = message;
    }

    @Override
    public void accept(NamespaceUnloaded message) {
        object = message;
    }

    @Override
    public void accept(YouSpectateGame message) {
        object = message;
    }

    @Override
    public void accept(StoppedSpectating message) {
        object = message;
    }

    @Override
    public void accept(YouLeftGame message) {
        object = message;
    }

    @Override
    public void accept(WelcomeToGame message) {
        object = message;
    }

    @Override
    public void accept(PlayerJoined message) {
        object = message;
    }

    @Override
    public void accept(PlayerLeft message) {
        object = message;
    }

    @Override
    public void accept(TurnEndedByServer message) {
        object = message;
    }

    @Override
    public void accept(VersionReply message) {
        object = message;
    }

    @Override
    public void accept(WhoAmIReply message) {
        object = message;
    }

    @Override
    public void accept(ClientReply message) {
        object = message;
    }

    @Override
    public void accept(GamesReply message) {
        object = message;
    }

    @Override
    public void accept(PlayersOfGameReply message) {
        object = message;
    }

    @Override
    public void accept(ModulesReply message) {
        object = message;
    }

    @Override
    public void accept(MyNamespacesReply message) {
        object = message;
    }

    @Override
    public void accept(DetailsOfGameReply message) {
        object = message;
    }

    public Object getObject() {
        return object;
    }

    public boolean hasError() {
        return hasError;
    }
}
