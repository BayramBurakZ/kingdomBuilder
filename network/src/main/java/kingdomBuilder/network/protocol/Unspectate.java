package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to stop spectating the game you spectate.
 */
@Protocol(format = "unspectate")
public record Unspectate() {
}
