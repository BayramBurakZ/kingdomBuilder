package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when client left the server.
 */
@Protocol(format="[SERVER_MESSAGE] [CLIENT_LEFT] <[#{clientData}]>")
public record ClientLeft(
    ClientData clientData
) {}
