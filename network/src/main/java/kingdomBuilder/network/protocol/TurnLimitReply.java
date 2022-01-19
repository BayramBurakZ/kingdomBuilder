package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains limit on the number of turns -
 * reply message ?turnlimit {@link kingdomBuilder.network.protocol.TurnLimitRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?turnlimit) <#{turnLimit}>")
public record TurnLimitReply(
        int turnLimit
) {
}
