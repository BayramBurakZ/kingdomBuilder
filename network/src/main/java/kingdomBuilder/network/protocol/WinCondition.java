package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the win conditions of this game.
 */
@Protocol(format = "[GAME_MESSAGE] [WIN_CONDITION] <[#{winCondition1};#{winCondition2};#{winCondition3}]>")
public record WinCondition(
        String winCondition1,
        String winCondition2,
        String winCondition3
) {
}
