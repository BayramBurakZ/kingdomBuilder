package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?whoami")
public record WhoAmIRequest() {
}
