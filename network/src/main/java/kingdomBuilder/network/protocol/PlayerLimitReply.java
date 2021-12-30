package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?playerlimit) <#{playerLimit}>")
public record PlayerLimitReply(
        int playerLimit
) {
}
