package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?winconditions) <[#{winCondition1};#{winCondition2};#{winCondition3}]>")
public record WinConditionReply(
        String winCondition1,
        String winCondition2,
        String winCondition3
) {
}
