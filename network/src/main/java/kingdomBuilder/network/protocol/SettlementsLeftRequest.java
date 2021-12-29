package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?settlementsleft")
public record SettlementsLeftRequest() {
}
