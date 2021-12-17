package kingdomBuilder.networkOutdated.protocol.tuples;

import kingdomBuilder.networkOutdated.internal.MessageFormat;

@MessageFormat(format="[#{clientId};#{name};#{gameId}]")
public record ClientTuple(
        int clientId,
        String name,
        int gameId
) { }
