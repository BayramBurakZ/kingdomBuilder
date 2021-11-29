package kingdomBuilder.network.protocol;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [MESSAGE] <[#{clientId};#{receiverIds};#{message}]>")
public record Message(
    int clientId,
    Integer[] receiverIds,
    String message
) {}
