package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when token was given to a client.
 */
@Protocol(format = "[GAME_MESSAGE] [TOKEN_RECEIVED] <[#{clientId};#{tokenType};#{row};#{column}]>")
public record TokenReceived(
        int clientId,
        String tokenType,
        int row,
        int column
) {
}
