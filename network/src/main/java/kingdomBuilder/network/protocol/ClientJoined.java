package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when new client authenticated at the server.
 */
@Protocol(format = "[SERVER_MESSAGE] [CLIENT_JOINED] <[#{clientData}]>")
public record ClientJoined(
    ClientData clientData
) {}
