package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when game started initialization.
 */
@Protocol(format = "[GAME_MESSAGE] [INIT_START]")
public record InitStart() {
}
