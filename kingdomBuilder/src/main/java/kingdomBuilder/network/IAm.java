package kingdomBuilder.network;

import kingdomBuilder.network.internal.Message;

@Message(format = "iam #{name}")
public record IAm(
    String name
) {}
