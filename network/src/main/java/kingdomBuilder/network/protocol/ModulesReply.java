package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains all registered modules and their namespaces -
 * reply message ?modules {@link kingdomBuilder.network.protocol.ModulesRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?modules) <{#{modules}}>")
public record ModulesReply(
        List<ModuleData> modules
) {}
