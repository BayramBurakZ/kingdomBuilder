package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?whoseturn) <#{clientId}>")
public record WhoseTurnReply(
        int clientId
) {
}
