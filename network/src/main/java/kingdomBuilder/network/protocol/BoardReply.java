package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?board) <#{boardData}>")
public record BoardReply(
        BoardData boardData
) {
}
