package se.raxa.plugin.nexasl.tests;

import org.junit.Test;
import se.raxa.plugin.nexasl.NexaSL;
import se.raxa.server.devices.helpers.Status;

import java.text.MessageFormat;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSLTest {
    private static TestNexaSL testNexaSL = new TestNexaSL();

    @Test
    public void testEncodeMessageOn() throws Exception {
        assertEquals(
                MessageFormat.format(
                        "{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}",
                        (char) 29,
                        (char) 17,
                        (char) 92
                ),
                testNexaSL.encodeMessage(Status.On)
        );
    }

    @Test
    public void testEncodeMessageOff() throws Exception {
        assertEquals(
                MessageFormat.format(
                        "{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}",
                        (char) 29,
                        (char) 17,
                        (char) 92
                ),
                testNexaSL.encodeMessage(Status.Off)
        );
    }

    @Test
    public void testEncodeMessageDim() throws Exception {
        assertEquals(
                MessageFormat.format(
                        "{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}",
                        (char) 29,
                        (char) 17,
                        (char) 92
                ),
                testNexaSL.encodeMessage(Status.Dim)
        );
    }

    @Test
    public void testEncodeMessageDimLevel() throws Exception {
        assertEquals(
                MessageFormat.format(
                        "{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}{0}{2}{0}{1}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{1}{0}{2}{0}{2}",
                        (char) 29,
                        (char) 17,
                        (char) 92
                ),
                testNexaSL.encodeMessage(Status.DimLevel, (byte) 15)
        );
    }

    private static class TestNexaSL implements NexaSL {

        @Override
        public long getSenderID() {
            return 1986456;
        }
    }
}
