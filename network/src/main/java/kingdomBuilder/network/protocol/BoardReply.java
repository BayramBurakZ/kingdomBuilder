package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?board) <[#{quadrantId1};#{quadrantId2};#{quadrantId3};#{quadrantId4}]>")
public record BoardReply(
        int quadrantId1,
        int quadrantId2,
        int quadrantId3,
        int quadrantId4
) {
}
