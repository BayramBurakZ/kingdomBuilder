package kingdomBuilder.networkOutdated.protocol.server;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="[SERVER_MESSAGE] [YOU_HAVE_BEEN_KICKED]")
public record YouHaveBeenKicked() {}
