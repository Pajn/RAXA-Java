package se.raxa.server.devices.helpers;

import com.mongodb.BasicDBObject;
import se.raxa.server.exceptions.ClassCreationException;

/**
 * @author Rasmus Eneman
 */
public abstract class Output extends Device {

    /**
     * @param clazz The class of the Connector
     *
     * @return The Outputs Connector or null if not available
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public <T extends Connector> T getConnector(Class<T> clazz) throws ClassCreationException {
        Object obj = getDbObj().get("connector");
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
    public Connector getConnector() throws ClassCreationException {
        return getConnector(Connector.class);
    }
}
