package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests information of all clients registered at the server.
 */
@Protocol(format = "?clients")
public record ClientsRequest() {}
