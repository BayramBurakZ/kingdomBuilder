package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [TOKEN_LOST] <[#{clientId};#{tokenType};#{row};#{column}]>")
public record TokenLost(
        int clientId,
        String tokenType,
        int row,
        int column
) {
}
