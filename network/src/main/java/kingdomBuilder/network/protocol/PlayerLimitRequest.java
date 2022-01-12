package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the player-limit of the current game.
 */
@Protocol(format = "?playerlimit")
public record PlayerLimitRequest() {
}
