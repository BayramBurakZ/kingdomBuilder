package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?games) <{#{games}}>")
public record GamesReply(
        List<GameData> games
) {}
