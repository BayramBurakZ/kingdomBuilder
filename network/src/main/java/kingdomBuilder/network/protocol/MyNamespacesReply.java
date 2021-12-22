package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?mymodules) <{#{modules}}>")
public record MyNamespacesReply(
    List<ModuleData> modules
) {}
