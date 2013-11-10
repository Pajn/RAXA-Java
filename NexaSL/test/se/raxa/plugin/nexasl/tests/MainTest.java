package se.raxa.plugin.nexasl.tests;

import org.junit.Test;
import se.raxa.plugin.nexasl.Main;
import se.raxa.server.plugins.Plugin;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class MainTest {
    @Test
    public void testGetName() throws Exception {
        Plugin plugin = new Main();
        assertEquals("Nexa Self Learning", plugin.getName());
    }
}
