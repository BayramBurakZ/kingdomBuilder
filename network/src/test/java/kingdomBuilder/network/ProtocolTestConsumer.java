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

    public Object getObject() {
        return object;
    }

    public boolean hasError() {
        return hasError;
    }
}
