package se.raxa.plugin.tellsticknet.tests;

import com.mongodb.BasicDBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.plugin.tellsticknet.TellstickNetService;
import se.raxa.server.devices.helpers.Device;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * @author Rasmus Eneman
 */
@PrepareForTest(TellstickNetService.class)
@RunWith(PowerMockRunner.class)
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
        tellstickNet.getDbObj().put("version", "RAXA1");
    }

    @Test
    public void testSend() throws Exception {
        PowerMockito.spy(TellstickNetService.class);
        ArgumentCaptor<String> code = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        PowerMockito.doNothing().when(TellstickNetService.class, "send", code.capture(), message.capture());

        tellstickNet.send("test message", 20, 20);
        assertEquals("ABCDEF", code.getValue());
        assertEquals("4:sendh1:SC:test message1:Pi14s1:Ri14ss", message.getValue());
    }

    @Test
    public void testSendDefault() throws Exception {
        PowerMockito.spy(TellstickNetService.class);
        ArgumentCaptor<String> code = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        PowerMockito.doNothing().when(TellstickNetService.class, "send", code.capture(), message.capture());

        tellstickNet.send("MoreTesting");
        assertEquals("ABCDEF", code.getValue());
        assertEquals("4:sendh1:SB:MoreTesting1:PiAs1:Ri5ss", message.getValue());
    }
}
