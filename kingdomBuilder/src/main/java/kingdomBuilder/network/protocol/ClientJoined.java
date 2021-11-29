package kingdomBuilder.network.protocol;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [CLIENT_JOINED] <[#{clientId};#{name};#{gameId}]>")
public record ClientJoined(
    int clientId,
    String name,
    int gameId
) {}
