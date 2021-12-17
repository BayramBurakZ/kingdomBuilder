package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [MESSAGE] <[#{clientId};#{receiverIds};#{message}]>")
public record Message(
    int clientId,
    Integer[] receiverIds,
    String message
) {}
