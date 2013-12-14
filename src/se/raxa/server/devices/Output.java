package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;

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
            setConnector(new ObjectId(kwargs.get("connector")));
        } else {
            if (!getSupportedConnectors().contains(null)) {
                throw new IllegalArgumentException("A connector is required by this plugin");
            }
        }
    }

    /**
     * Called when the device is updated
     *
     * @param kwargs A map with arguments to update
     *
     * @throws IllegalArgumentException If at least one of the kwargs are invalid
     */
    public default void onUpdate(Map<String, String> kwargs) throws IllegalArgumentException {
        Device.super.onUpdate(kwargs);

        if (kwargs.containsKey("connector")) {
            setConnector(new ObjectId(kwargs.get("connector")));
        }
    }

    /**
     * Called when the device should be presented
     *
     * @return A map with details that should be outputted
     */
    @Override
    public default Map<String, Object> describe() {
        Map<String, Object> map = Device.super.describe();

        map.put("connector", getDBObj().get("connector"));

        return map;
    }

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
            getDBObj().put("connector", connector.describe());
        } else {
            throw new IllegalArgumentException("Connector is not supported");
        }
    }
}
