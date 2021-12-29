package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "shutdown server")
public record ShutdownServer() {
}
