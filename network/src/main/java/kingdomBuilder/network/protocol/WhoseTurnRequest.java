package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the player whose turn it is.
 */
@Protocol(format = "?whoseturn")
public record WhoseTurnRequest() {
}
