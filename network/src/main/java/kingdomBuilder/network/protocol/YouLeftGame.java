package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [YOU_LEFT_GAME] <#{gameId}>")
public record YouLeftGame(
        int gameId
) {}
