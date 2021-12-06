package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="chat [#{receiverIds};#{message}]")
public record Chat(
    Integer[] receiverIds,
    String message
) {}
