package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [CLIENT_LEFT] <[#{clientId};#{name};#{gameId}]>")
public record ClientLeft(
    int clientId,
    String name,
    int gameId
) {}
