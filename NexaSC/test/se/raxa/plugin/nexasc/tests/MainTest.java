package se.raxa.plugin.nexasc.tests;

import org.junit.Test;
import se.raxa.plugin.nexasc.Main;
import se.raxa.server.plugins.Plugin;

import static org.junit.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class MainTest {
    @Test
    public void testGetName() throws Exception {
        Plugin plugin = new Main();
        assertEquals("Nexa Switch Case", plugin.getName());
    }
}
