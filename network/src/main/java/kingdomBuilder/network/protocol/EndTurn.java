package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that ends the current turn.
 */
@Protocol(format = "end turn")
public record EndTurn() {
}
