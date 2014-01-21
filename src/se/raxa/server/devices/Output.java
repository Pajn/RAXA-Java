package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;
import se.raxa.server.plugins.devices.GetProperty;
import se.raxa.server.plugins.devices.SetProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static se.raxa.server.devices.helpers.Devices.createDeviceFromDbObject;
import static se.raxa.server.devices.helpers.Devices.getDeviceById;

/**
 * @author Rasmus Eneman
 */
public interface Output extends Device {

    /**
     * @return A list of supported Connector classes, contains null if supports not having one
     */
    public default List<Class> getSupportedConnectors() {
        List <Class> classes = new ArrayList<>();

        classes.add(null);

        return classes;
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

    /**
     * @return The Outputs Connector as a property value or null if not available
     */
    @GetProperty("connector")
    public default Map<String, Object> getConnectorProperty() {
        try {
            Connector connector = getConnector(Connector.class);
            return connector == null ? null : connector.read();
        } catch (ClassCreationException | BadPluginException e) {
            e.printStackTrace(); //TODO handle in a good way
            return null;
        }
    }

    public default void setConnector(ObjectId id) {
        boolean isSupported = false;
        Device connector;

        try {
            connector = getDeviceById(id);

            for (Class clazz : getSupportedConnectors()) {
                if (clazz.isInstance(connector)) {
                    isSupported = true;
                }
            }
        } catch (ClassCreationException | NotFoundException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Connector not found");
        }

        if (isSupported) {
            getDBObj().put("connector", connector.getDBObj());
        } else {
            throw new IllegalArgumentException("Connector is not supported");
        }
    }

    @SetProperty(value = "connector", required = false)
    public default void setConnector(String id) {
        if (!getSupportedConnectors().contains(null)) {
            throw new IllegalArgumentException("A connector is required for this device");
        }
        setConnector(new ObjectId(id));
    }
}
