package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to shut down server.
 */
@Protocol(format = "shutdown server")
public record ShutdownServer() {
}
