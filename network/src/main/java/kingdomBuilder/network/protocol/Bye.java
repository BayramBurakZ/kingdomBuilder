package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the message to logout from server.
 */
@Protocol(format = "bye")
public record Bye() {}
