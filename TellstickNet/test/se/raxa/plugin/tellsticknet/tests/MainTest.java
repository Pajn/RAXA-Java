package se.raxa.plugin.tellsticknet.tests;

import org.junit.Test;
import se.raxa.plugin.tellsticknet.Main;
import se.raxa.server.plugins.Plugin;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class MainTest {
    @Test
    public void testGetName() throws Exception {
        Plugin plugin = new Main();
        assertEquals("TellstickNet", plugin.getName());
    }
}
