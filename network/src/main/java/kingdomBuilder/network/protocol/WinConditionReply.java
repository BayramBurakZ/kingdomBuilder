package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the win conditions of the current game -
 * reply message ?winconditions {@link kingdomBuilder.network.protocol.WinConditionsRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?winconditions) <[#{winCondition1};#{winCondition2};#{winCondition3}]>")
public record WinConditionReply(
        String winCondition1,
        String winCondition2,
        String winCondition3
) {
}
