package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.plugins.devices.DeviceClasses;
import se.raxa.server.plugins.devices.GetProperty;
import se.raxa.server.plugins.devices.SetProperty;

import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public interface Device {

    /**
     * Called when the device is about to be created
     *
     * @param propertyValues A map with values for all properties to be set
     *
     * @throws IllegalArgumentException if a property have an invalid value or
     *                                  if a required property isn't specified
     * @throws ClassCreationException If the device can't be created
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public default void create(Map<String, String> propertyValues) throws IllegalArgumentException, ClassCreationException,
            BadPluginException {
        DeviceClasses.getDescriptor(getClass()).setProperties(this, propertyValues, true);
    }

    /**
     * Called when the device is read
     *
     * @return A map with values for all properties
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public default Map<String, Object> read() throws BadPluginException {
        return DeviceClasses.getDescriptor(getClass()).getProperties(this);
    }

    /**
     * Called when the device is updated
     *
     * @param propertyValues A map with values for all properties to update
     *
     * @throws IllegalArgumentException if a property have an invalid value
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public default void update(Map<String, String> propertyValues) throws BadPluginException {
        DeviceClasses.getDescriptor(getClass()).setProperties(this, propertyValues, false);
    }

    /**
     * @return The inner DBObject
     */
    public abstract BasicDBObject getDBObj();

    /**
     * @param dbObject The inner DBObject
     */
    public abstract void setDBObj(BasicDBObject dbObject);

    /**
     * @return The unique id
     */
    public abstract ObjectId getId();

    /**
     * @return The unique id as a Mongod String
     */
    @GetProperty("id")
    public default String getIdProperty() {
        return getId().toStringMongod();
    }

    /**
     * @return The name
     */
    public abstract String getName();

    /**
     * @return The name
     */
    @GetProperty("name")
    public default String getNameProperty() {
        return getName();
    }

    /**
     * @param name The name to set
     *
     * @throws IllegalArgumentException If the name already is in use
     */
    public abstract void setName(String name) throws IllegalArgumentException;

    /**
     * @param name The name to set
     *
     * @throws IllegalArgumentException If the name already is in use
     */
    @SetProperty("name")
    public default void setNameProperty(String name) {
        setName(name);
    }

    /**
     * @return An array of types, ordered by rough position in tree
     */
    public default String[] getType() {
        return DeviceClasses.getDescriptor(getClass()).getTypes();
    }

    /**
     * Save to the database
     */
    public abstract void save();
}
