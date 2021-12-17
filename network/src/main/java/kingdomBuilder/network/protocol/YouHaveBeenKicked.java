package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format="[SERVER_MESSAGE] [YOU_HAVE_BEEN_KICKED]")
public record YouHaveBeenKicked() {
}
