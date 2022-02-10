package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents a message that a token has been used by a player.
 */
@Protocol(format = "[GAME_MESSAGE] [TOKEN_USED] <[#{clientId};#{tokenType}]>")
public record TokenUsed(
        int clientId,
        String tokenType
) {
}
