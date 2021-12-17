package kingdomBuilder.networkOutdated.protocol.server.reply;

import kingdomBuilder.networkOutdated.internal.MessageFormat;
import kingdomBuilder.networkOutdated.protocol.tuples.ClientTuple;

@MessageFormat(format="[REPLY_MESSAGE] (?clients) <#{clients}>")
public record ReplyClients(
        ClientTuple[] clients
) { }
