package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format = "iam #{name}")
public record IAm(
    String name
) {}
