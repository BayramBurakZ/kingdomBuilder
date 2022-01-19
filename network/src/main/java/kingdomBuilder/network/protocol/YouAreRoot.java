package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiver has been promoted to root.
 */
@Protocol(format = "[SERVER_MESSAGE] [YOU_ARE_ROOT]")
public record YouAreRoot() {
}
