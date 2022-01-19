package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests limit on the number of turns.
 */
@Protocol(format = "?turnlimit")
public record TurnLimitRequest() {
}
