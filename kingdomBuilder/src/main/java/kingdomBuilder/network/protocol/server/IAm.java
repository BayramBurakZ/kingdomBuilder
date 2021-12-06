package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format = "iam #{name}")
public record IAm(
    String name
) {}
