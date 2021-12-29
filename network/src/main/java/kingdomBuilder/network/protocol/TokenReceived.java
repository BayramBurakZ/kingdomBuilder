package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [TOKEN_RECEIVED] <[#{clientId};#{tokenType};#{row};#{column}]>")
public record TokenReceived(
        int clientId,
        String tokenType,
        int row,
        int column
) {
}
