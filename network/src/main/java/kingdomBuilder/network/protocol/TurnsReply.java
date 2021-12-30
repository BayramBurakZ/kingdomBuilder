package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?turns) <#{turns}>")
public record TurnsReply(
        int turns
) {
}
