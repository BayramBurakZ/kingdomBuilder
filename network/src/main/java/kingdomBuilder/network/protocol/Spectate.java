package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to spectate a game.
 */
@Protocol(format = "spectate #{gameId}")
public record Spectate(
        int gameId
) {
}
