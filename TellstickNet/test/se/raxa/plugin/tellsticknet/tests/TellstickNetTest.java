package se.raxa.plugin.tellsticknet.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.helpers.Device;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class TellstickNetTest {
    private static TellstickNet tellstickNet;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("code", "ABCDEF");
        dbObject.put("version", "RAXA1");
        tellstickNet = Device.createDeviceFromDbObject(TellstickNet.class, dbObject);
    }


    @Test
    public void testGetType() throws Exception {
        assertEquals(true, Arrays.equals(new String[]{"tellsticknet", "Connector"}, tellstickNet.getType()));
    }

    @Test
    public void testGetCode() throws Exception {
        assertEquals("ABCDEF", tellstickNet.getCode());
    }

    @Test
    public void testGetVersion() throws Exception {
        assertEquals("RAXA1", tellstickNet.getVersion());
    }

    @Test
    public void testIsUsable() throws Exception {
        assertEquals(true, tellstickNet.isUsable());
        tellstickNet.getDbObj().put("version", "TN2");
        assertEquals(false, tellstickNet.isUsable());
    }
}
