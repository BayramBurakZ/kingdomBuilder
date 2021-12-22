package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [TURN_ENDED_BY_SERVER]")
public record TurnEndedByServer() {}
