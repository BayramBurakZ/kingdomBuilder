package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to join a game.
 */
@Protocol(format = "join #{gameId}")
public record Join(
        int gameId
) {
}
