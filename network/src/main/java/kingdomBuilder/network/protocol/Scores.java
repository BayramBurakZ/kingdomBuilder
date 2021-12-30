package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[GAME_MESSAGE] [SCORES] <{#{scoresDataList}}>")
public record Scores(
        List<ScoresData> scoresDataList
) {
}
