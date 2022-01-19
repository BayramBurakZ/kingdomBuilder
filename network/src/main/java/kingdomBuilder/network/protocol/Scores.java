package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains scores of the players.
 */
@Protocol(format = "[GAME_MESSAGE] [SCORES] <{#{scoresDataList}}>")
public record Scores(
        List<ScoresData> scoresDataList
) {
}
