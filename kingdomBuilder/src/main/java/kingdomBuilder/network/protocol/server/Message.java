package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [MESSAGE] <[#{clientId};#{receiverIds};#{message}]>")
public record Message(
    int clientId,
    Integer[] receiverIds,
    String message
) {}
