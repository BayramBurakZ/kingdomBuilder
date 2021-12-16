package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format="[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[#{clientId};#{name};#{gameId}]>")
public record WelcomeToServer(
        int clientId,
        String name,
        int gameId
) {}
