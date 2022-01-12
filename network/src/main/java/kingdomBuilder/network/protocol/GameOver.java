package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that will be sent when the game is over and lists the client-IDs of the winners.
 */
@Protocol(format = "[GAME_MESSAGE] [GAME_OVER] <{#{clientIds}}>")
public record GameOver(
        List<Integer> clientIds
) {
}
