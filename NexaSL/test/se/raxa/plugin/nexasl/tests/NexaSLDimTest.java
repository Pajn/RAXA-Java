package se.raxa.plugin.nexasl.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.nexasl.NexaSLDim;
import se.raxa.server.devices.helpers.Device;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSLDimTest {
    private static NexaSLDim nexaSLDim;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSLDim = Device.createDeviceFromDbObject(NexaSLDim.class, new BasicDBObject());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(true,
                Arrays.equals(new String[]{"NexaSLDim", "NexaSL", "DimmableByLevel", "DimmableByTime", "Lamp", "Output"},
                              nexaSLDim.getType()));
    }

    @Test
    public void testGetDimLevelMax() throws Exception {
        assertEquals(15, nexaSLDim.getDimLevelMax());
    }

    @Test
    public void testGetDimLevel() throws Exception {
        nexaSLDim.getDbObj().put("dim_level", 12);
        assertEquals(12, nexaSLDim.getDimLevel());
    }
}
