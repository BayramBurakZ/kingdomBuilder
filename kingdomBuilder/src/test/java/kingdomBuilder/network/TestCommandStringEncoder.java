package kingdomBuilder.network;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestCommandStringEncoder {

    @Test
    public void rightAnswer() {
        int answer = 42;
        assertEquals(answer, 42);
    }

    @Test
    public void failingTest() {
        assertTrue(false);
    }

}
