package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [YOU_SPECTATE_GAME] <#{gameId}>")
public record YouSpectateGame(
        int gameId
) {}
