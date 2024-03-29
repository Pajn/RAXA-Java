package se.raxa.plugin.nexasl.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import se.raxa.plugin.nexasl.NexaSLOnOff;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;

import static org.junit.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOffTest {
    private static NexaSLOnOff nexaSLOnOff;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSLOnOff = Devices.createDeviceFromDbObject(NexaSLOnOff.class, new BasicDBObject("sender_id", 81234));
    }

    @Test
    public void testGetSenderID() throws Exception {
        assertEquals(81234, nexaSLOnOff.getSenderID());
    }

    @Test
    public void testIsTurnedOn() throws Exception {
        nexaSLOnOff.getDBObj().put("status", Status.On.getValue());
        assertEquals(true, nexaSLOnOff.isTurnedOn());
        nexaSLOnOff.getDBObj().put("status", Status.Off.getValue());
        assertEquals(false, nexaSLOnOff.isTurnedOn());
    }
}
