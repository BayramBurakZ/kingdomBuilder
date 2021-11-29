package kingdomBuilder.network.protocol;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="chat [#{receiverIds};#{message}]")
public record Chat(
    Integer[] receiverIds,
    String message
) {}
