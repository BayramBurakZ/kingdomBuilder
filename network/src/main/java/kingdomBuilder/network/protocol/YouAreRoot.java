package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [YOU_ARE_ROOT]")
public record YouAreRoot() {
}
