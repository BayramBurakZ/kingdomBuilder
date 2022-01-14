package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the data of client.
 */
@Protocol(format = "[#{clientId};#{name};#{gameId}]", isComponent = true)
public record ClientData(
    int clientId,
    String name,
    int gameId
) {}
