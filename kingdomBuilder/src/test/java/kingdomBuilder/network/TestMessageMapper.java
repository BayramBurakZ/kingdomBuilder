package kingdomBuilder.network;

import kingdomBuilder.network.internal.MessageMapper;

public class TestMessageMapper {

    public void testMappingIAm() {
        IAm msg = new IAm("Test");
        String str = MessageMapper.mapToString(msg);
    }

}
