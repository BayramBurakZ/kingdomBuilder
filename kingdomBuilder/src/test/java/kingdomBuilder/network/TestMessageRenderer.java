package kingdomBuilder.network;

import kingdomBuilder.networkOutdated.internal.MessageFormatRenderer;

import static org.junit.jupiter.api.Assertions.*;

import kingdomBuilder.networkOutdated.protocol.server.Chat;
import kingdomBuilder.networkOutdated.protocol.server.IAm;
import org.junit.jupiter.api.Test;


public class TestMessageRenderer {

    @Test
    public void testRenderingIAm() {
        MessageFormatRenderer renderer = new MessageFormatRenderer();

        IAm msg = new IAm("Test");
        String str = renderer.render(msg);

        assertEquals(str, "iam Test");
    }

    @Test
    public void testRenderingChat() {
        MessageFormatRenderer renderer = new MessageFormatRenderer();
        Chat msg = new Chat(new Integer[]{2, 3, 42}, "Hallo Du!");
        String str = renderer.render(msg);

        assertEquals("chat [{2,3,42};Hallo Du!]", str);
    }

}
