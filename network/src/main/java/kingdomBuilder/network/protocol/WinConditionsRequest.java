package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the win conditions of the current game.
 */
@Protocol(format = "?winconditions")
public record WinConditionsRequest() {
}
