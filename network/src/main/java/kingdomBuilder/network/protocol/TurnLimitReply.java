package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?turnlimit) <#{turnLimit}>")
public record TurnLimitReply(
        int turnLimit
) {
}
