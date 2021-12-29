package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [TURN_START] <#{client id}>")
public record TurnStart(
        int clientId
) {
}
