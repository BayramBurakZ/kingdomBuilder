package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that should be sent to receiver.
 */
@Protocol(format = "chat [#{receiverIds};#{message}]")
public record Chat(
    List<Integer> receiverIds,
    String message
) {}
