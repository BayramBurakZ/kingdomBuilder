package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when the receiver has been kicked from the server.
 */
@Protocol(format="[SERVER_MESSAGE] [YOU_HAVE_BEEN_KICKED]")
public record YouHaveBeenKicked() {
}
