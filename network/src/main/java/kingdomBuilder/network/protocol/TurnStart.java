package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when turn started.
 */
@Protocol(format = "[GAME_MESSAGE] [TURN_START] <#{client id}>")
public record TurnStart(
        int clientId
) {
}
