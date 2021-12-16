package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "chat [#{receiverIds};#{message}]")
public record Chat(
    List<Integer> receiverIds,
    String message
) {}
