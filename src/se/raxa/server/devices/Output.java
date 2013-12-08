package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.Map;

import static se.raxa.server.devices.helpers.Devices.createDeviceFromDbObject;

/**
 * @author Rasmus Eneman
 */
public interface Output extends Device {

    /**
     * Called when a new object is created
     *
     * @param kwargs A map with arguments for creation
     *
     * @throws ClassCreationException If the class somehow can't be created
     * @throws IllegalArgumentException If at least one of the kwargs are invalid (missing or illegal value)
     *
     */
    @Override
    public default void onCreate(Map<String, String> kwargs) throws ClassCreationException, IllegalArgumentException {
        Device.super.onCreate(kwargs);

        if (kwargs.containsKey("connector")) {
            getDBObj().put("connector", kwargs.get("connector"));
        }
    }

    /**
     * @param clazz The class of the Connector
     *
     * @return The Outputs Connector or null if not available
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public default <T extends Connector> T getConnector(Class<T> clazz) throws ClassCreationException {
        Object obj = getDBObj().get("connector");
        if (obj == null) {
            return null;
        }
        return createDeviceFromDbObject(clazz, (BasicDBObject) obj);
    }

    /**
     * @return The Outputs Connector or null if not available
     *
     * @throws ClassCreationException
     */
    public default Connector getConnector() throws ClassCreationException {
        return getConnector(Connector.class);
    }
}
