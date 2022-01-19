package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains all namespaces currently registered for the requesting client -
 * reply message ?mymodules {@link kingdomBuilder.network.protocol.MyModulesRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?mymodules) <{#{modules}}>")
public record MyNamespacesReply(
    List<ModuleData> modules
) {}
