package se.raxa.plugin.nexasc.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.nexasc.NexaSCOnOff;
import se.raxa.server.devices.helpers.Device;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOffTest {
    private static NexaSCOnOff nexaSCOnOff;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSCOnOff = Device.createDeviceFromDbObject(NexaSCOnOff.class, new BasicDBObject());
        nexaSCOnOff.setHouseDevice((byte) 13, (byte) 2);
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(true, Arrays.equals(new String[]{"NexaSCOnOff", "Lamp", "Output"}, nexaSCOnOff.getType()));
    }

    @Test
    public void testGetHouse() throws Exception {
        assertEquals((byte) 13, nexaSCOnOff.getHouse());
    }

    @Test
    public void testGetDevice() throws Exception {
        assertEquals((byte) 2, nexaSCOnOff.getDevice());
    }

    @Test
    public void testIsTurnedOn() throws Exception {
        nexaSCOnOff.getDbObj().put("status", Status.On.ordinal());
        assertEquals(true, nexaSCOnOff.isTurnedOn());
        nexaSCOnOff.getDbObj().put("status", Status.Off.ordinal());
        assertEquals(false, nexaSCOnOff.isTurnedOn());
    }
}
