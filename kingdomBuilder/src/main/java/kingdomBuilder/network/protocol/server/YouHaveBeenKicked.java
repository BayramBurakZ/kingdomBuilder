package kingdomBuilder.network.protocol.server;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [YOU_HAVE_BEEN_KICKED]")
public record YouHaveBeenKicked() {}
