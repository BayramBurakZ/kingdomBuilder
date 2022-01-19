package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the player information of the current game.
 */
@Protocol(format = "?players")
public record PlayersRequest() {
}
