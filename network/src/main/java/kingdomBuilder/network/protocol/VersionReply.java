package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the server version -
 * reply message ?version {@link kingdomBuilder.network.protocol.VersionRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?version) <#{serverVersion}>")
public record VersionReply(
        String serverVersion
) {}
