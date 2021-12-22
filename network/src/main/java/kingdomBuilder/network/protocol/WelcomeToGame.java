package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [WELCOME_TO_GAME] <#{gameId}>")
public record WelcomeToGame(
        int gameId
) {}
