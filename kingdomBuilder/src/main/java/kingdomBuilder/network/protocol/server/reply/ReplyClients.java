package kingdomBuilder.network.protocol.server.reply;

import kingdomBuilder.network.internal.MessageFormat;
import kingdomBuilder.network.protocol.tuples.ClientTuple;

@MessageFormat(format="[REPLY_MESSAGE] (?clients) <#{clients}>")
public record ReplyClients(
        ClientTuple[] clients
) { }
