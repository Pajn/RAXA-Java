package se.raxa.plugin.nexasc.tests;

import org.junit.Test;
import se.raxa.plugin.nexasc.NexaSC;
import se.raxa.server.devices.helpers.Status;

import static org.junit.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSCTest {
    private static final NexaSC testNexaSC = new TestNexaSC();

    @Test
    public void testGetCodeSwitchTuple() throws Exception {
        assertEquals("$kk$$kk$$kk$$kk$", testNexaSC.getCodeSwitchTuple((byte) 15));
    }

    @Test
    public void testEncodeMessageOn() throws Exception {
        assertEquals("$kk$$kk$$k$k$k$k$k$k$kk$$kk$$k$k$k$k$kk$$kk$$kk$$k", testNexaSC.encodeMessage(Status.On));
    }

    @Test
    public void testEncodeMessageOff() throws Exception {
        assertEquals("$kk$$kk$$k$k$k$k$k$k$kk$$kk$$k$k$k$k$kk$$kk$$k$k$k", testNexaSC.encodeMessage(Status.Off));
    }

    @Test
    public void testEncodeMessageDim() throws Exception {
        assertEquals("$kk$$kk$$k$k$k$k$k$k$kk$$kk$$k$k$k$k$kk$$kk$$kk$$k", testNexaSC.encodeMessage(Status.Dim));
    }

    private static class TestNexaSC implements NexaSC {

        @Override
        public byte getHouse() {
            return 3;
        }

        @Override
        public byte getDevice() {
            return 6;
        }
    }
}
