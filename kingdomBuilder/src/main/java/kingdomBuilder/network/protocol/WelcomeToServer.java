package kingdomBuilder.network.protocol;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[#{clientId};#{name};#{gameId}]>")
public record WelcomeToServer(
    int clientId,
    String name,
    int gameId
) {}
