package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that replys the player-limit of the current game -
 * reply message ?playerlimit {@link kingdomBuilder.network.protocol.PlayerLimitRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?playerlimit) <#{playerLimit}>")
public record PlayerLimitReply(
        int playerLimit
) {
}
