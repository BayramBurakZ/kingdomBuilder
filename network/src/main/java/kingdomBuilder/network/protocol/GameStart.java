package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when the game started.
 */
@Protocol(format = "[GAME_MESSAGE] [GAME_START]")
public record GameStart() {
}
