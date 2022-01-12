package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests current turn number.
 */
@Protocol(format = "?turns")
public record TurnsRequest() {
}
