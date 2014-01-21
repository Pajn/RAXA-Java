package se.raxa.plugin.nexasc.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import se.raxa.plugin.nexasc.NexaSCDim;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.exceptions.ClassCreationException;

/**
 * @author Rasmus Eneman
 */
public class NexaSCDimTest {
    private static NexaSCDim nexaSCDim;

    @BeforeClass
    public static void oneTimeSetUp() throws ClassCreationException {
        nexaSCDim = Devices.createDeviceFromDbObject(NexaSCDim.class, new BasicDBObject());
    }
}
