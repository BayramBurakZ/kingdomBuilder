package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a turn has been ended by the server.
 */
@Protocol(format = "[SERVER_MESSAGE] [TURN_ENDED_BY_SERVER]")
public record TurnEndedByServer() {}
