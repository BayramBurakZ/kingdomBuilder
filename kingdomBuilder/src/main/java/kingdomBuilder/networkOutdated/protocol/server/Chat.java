package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="chat [#{receiverIds};#{message}]")
public record Chat(
    Integer[] receiverIds,
    String message
) {}
