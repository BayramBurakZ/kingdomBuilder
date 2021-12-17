package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [CLIENT_LEFT] <[#{clientId};#{name};#{gameId}]>")
public record ClientLeft(
    int clientId,
    String name,
    int gameId
) {}
