package kingdomBuilder.network.protocol.tuples;

import kingdomBuilder.network.internal.MessageFormat;

@MessageFormat(format="[#{clientId};#{name};#{gameId}]")
public record ClientTuple(
        int clientId,
        String name,
        int gameId
) { }
