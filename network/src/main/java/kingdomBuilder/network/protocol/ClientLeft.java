package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format="[SERVER_MESSAGE] [CLIENT_LEFT] <[#{clientId};#{name};#{gameId}]>")
public record ClientLeft(
        int clientId,
        String name,
        int gameId
) {}
