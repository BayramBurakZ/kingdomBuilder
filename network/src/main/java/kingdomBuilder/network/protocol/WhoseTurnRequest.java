package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?whoseturn")
public record WhoseTurnRequest() {
}
