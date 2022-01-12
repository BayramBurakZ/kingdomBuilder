package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiver was registered at the server.
 */
@Protocol(format="[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[#{clientId};#{name};#{gameId}]>")
public record WelcomeToServer(
        int clientId,
        String name,
        int gameId
) {}
