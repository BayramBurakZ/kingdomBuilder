package kingdomBuilder.network;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestCommandStringEncoder {

    @Test
    public void testIAm() {
        CommandEncoder encoder = new CommandStringEncoder();
        String command = encoder.iam("Tester");

        assertNotNull(command);
        assertEquals(command, "iam Tester");
    }
}
