package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when the receiver is registered to game.
 */
@Protocol(format = "[SERVER_MESSAGE] [WELCOME_TO_GAME] <#{gameId}>")
public record WelcomeToGame(
        int gameId
) {}
