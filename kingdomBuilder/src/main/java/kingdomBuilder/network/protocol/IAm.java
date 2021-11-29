package kingdomBuilder.network.protocol;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format = "iam #{name}")
public record IAm(
    String name
) {}
