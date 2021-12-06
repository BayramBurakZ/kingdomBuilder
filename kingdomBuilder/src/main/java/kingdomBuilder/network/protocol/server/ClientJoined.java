package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;
import kingdomBuilder.network.protocol.tuples.ClientTuple;

@MessageFormat(format="[SERVER_MESSAGE] [CLIENT_JOINED] <[#{clientId};#{name};#{gameId}]>")
public record ClientJoined(
    int clientId,
    String name,
    int gameId
) { }
