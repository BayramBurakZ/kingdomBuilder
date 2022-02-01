package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to send "I am alive?" message that cuases no further actions.
 * reply message to ping {@link kingdomBuilder.network.protocol.Pong}
 */
@Protocol(format = "pong")
public record Pong () {}
