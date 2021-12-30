package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?timelimit) <#{timeLimit}>")
public record TimeLimitReply(
        int timeLimit
) {
}
