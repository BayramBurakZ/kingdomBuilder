package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[GAME_MESSAGE] [GAME_OVER] <{#{clientIds}}>")
public record GameOver(
        List<Integer> clientIds
) {
}
