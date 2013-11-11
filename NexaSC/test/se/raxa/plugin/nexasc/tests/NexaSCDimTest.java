package se.raxa.plugin.nexasc.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.nexasc.NexaSCDim;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSCDimTest {
    private static NexaSCDim nexaSCDim;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSCDim = Devices.createDeviceFromDbObject(NexaSCDim.class, new BasicDBObject());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(true, Arrays.equals(new String[]{"NexaSCDim", "DimmableByTime", "Lamp", "Output"}, nexaSCDim.getType()));
    }
}
