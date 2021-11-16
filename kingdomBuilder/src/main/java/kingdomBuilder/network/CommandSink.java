package kingdomBuilder.network;

import java.io.IOException;

public interface CommandSink {
    String sendCommandString(String command) throws IOException;
}
