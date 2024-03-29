package se.raxa.plugin.nexasc.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.nexasc.NexaSCOnOff;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;

import static org.junit.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOffTest {
    private static NexaSCOnOff nexaSCOnOff;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSCOnOff = Devices.createDeviceFromDbObject(NexaSCOnOff.class, new BasicDBObject());
        nexaSCOnOff.setHouse((byte) 13);
        nexaSCOnOff.setDevice((byte) 2);
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
        nexaSCOnOff.getDBObj().put("status", Status.On.getValue());
        assertEquals(true, nexaSCOnOff.isTurnedOn());
        nexaSCOnOff.getDBObj().put("status", Status.Off.getValue());
        assertEquals(false, nexaSCOnOff.isTurnedOn());
    }
}
