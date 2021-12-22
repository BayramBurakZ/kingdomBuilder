package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?version) <#{serverVersion}>")
public record VersionReply(
        String serverVersion
) {}
