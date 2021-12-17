package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [CLIENT_JOINED] <[#{clientId};#{name};#{gameId}]>")
public record ClientJoined(
    int clientId,
    String name,
    int gameId
) { }
