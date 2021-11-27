package kingdomBuilder.network;

import kingdomBuilder.network.internal.Message;

@Message(format="[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[#{clientId};#{name};#{gameId}]>")
public record IAmResponse(
    int clientId,
    String name,
    int gameId
) {}
