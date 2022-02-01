package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents a message that requests the board configuration.
 */
@Protocol(format = "?board")
public record BoardRequest() {
}
