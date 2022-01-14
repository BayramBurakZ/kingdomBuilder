package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the time-limit of the current game.
 */
@Protocol(format = "?timelimit")
public record TimeLimitRequest() {
}
