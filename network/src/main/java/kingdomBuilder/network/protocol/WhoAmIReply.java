package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?whoami) <[#{clientId};#{clientName};#{gameId}]>")
public record WhoAmIReply(
        int clientId,
        String clientName,
        int gameId
) {}
