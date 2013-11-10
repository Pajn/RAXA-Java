package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import se.raxa.server.exceptions.ClassCreationException;

import static se.raxa.server.devices.helpers.Devices.createDeviceFromDbObject;

/**
 * @author Rasmus Eneman
 */
public interface Output extends Device {
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
