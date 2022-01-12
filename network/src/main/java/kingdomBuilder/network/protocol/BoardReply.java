package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the reply message to the ?board-request message {@link kingdomBuilder.network.protocol.BoardRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?board) <#{boardData}>")
public record BoardReply(
        BoardData boardData
) {
}
