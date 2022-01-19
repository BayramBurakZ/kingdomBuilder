package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains time-limit of current game -
 * reply message ?timelimit {@link kingdomBuilder.network.protocol.TimeLimitRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?timelimit) <#{timeLimit}>")
public record TimeLimitReply(
        int timeLimit
) {
}
