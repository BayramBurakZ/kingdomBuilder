package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?modules) <{#{modules}}>")
public record ModulesReply(
        List<ModuleData> modules
) {}
