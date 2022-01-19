package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiving a "Are you still alive?" (pong-) message.
 * {@link kingdomBuilder.network.protocol.Pong}
 */
@Protocol(format = "[SERVER_MESSAGE] [PING]")
public record Ping () {}
