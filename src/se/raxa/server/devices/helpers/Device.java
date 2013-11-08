package se.raxa.server.devices.helpers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.Database;
import se.raxa.server.devices.Devices;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;

/**
 * @author Rasmus Eneman
 */
public abstract class Device {
    private BasicDBObject obj;

    protected Device() {
        obj = new BasicDBObject();
        obj.put("_id", new ObjectId());
        obj.put("type", getType());
    }

    /**
     * Create a Device object from a database object
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public static Device createDeviceFromDbObject(BasicDBObject obj) throws ClassCreationException {
        String type = (String) ((BasicDBList) obj.get("type")).get(0);
        Device device;
        try {
            device = Devices.getClasses().get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassCreationException(String.format("Error when creating Device from type '%s'", type), e);
        }
        device.obj = obj;
        return device;
    }

    /**
     * Create a Device object from a database object
     *
     * @param clazz The class of the Device
     *
     * @throws ClassCreationException
     */
    public static <T extends Device> T createDeviceFromDbObject(@SuppressWarnings("UnusedParameters") Class<T> clazz, BasicDBObject obj) throws
            ClassCreationException {
        //noinspection unchecked
        return (T) createDeviceFromDbObject(obj);
    }

    /**
     * Queries the database for one device using
     *
     * @param key The key of the value
     * @param value The value to search for
     *
     * @return A BasicDBObject for that device
     */
    private static BasicDBObject queryDatabaseSafe(String key, Object value) {
        BasicDBObject query = new BasicDBObject(key, value);
        query = (BasicDBObject) Database.devices().findOne(query);
        return query;
    }

    /**
     * Queries the database for one device using
     *
     * @param key The key of the value
     * @param value The value to search for
     *
     * @return A BasicDBObject for that device
     *
     * @throws NotFoundException If no device where found
     */
    private static BasicDBObject queryDatabase(String key, Object value) throws NotFoundException {
        BasicDBObject query = queryDatabaseSafe(key, value);
        if (query == null) {
            throw new NotFoundException(String.format("No device with the %s '%s' found", key, value.toString()));
        }
        return query;
    }

    /**
     * Gets a Device from the database using its id.
     *
     * @param clazz The class of the Device
     * @param id The MongoDB id of the Device
     *
     * @throws NotFoundException If no device where found
     * @throws ClassCreationException If the class couldn't be created
     */
    public static <T extends Device> T getDeviceById(Class<T> clazz, ObjectId id) throws NotFoundException,
            ClassCreationException {
        return createDeviceFromDbObject(clazz, queryDatabase("_id", id));
    }

    /**
     * Gets a Device from the database using its id.
     *
     * @param id The MongoDB id of the Device
     *
     * @throws NotFoundException If no device where found
     * @throws ClassCreationException If the class couldn't be created
     */
    public static Device getDeviceById(ObjectId id) throws NotFoundException, ClassCreationException {
        return getDeviceById(Device.class, id);
    }

    /**
     * Gets a Device from the database using name.
     *
     * @param clazz The class of the Device
     * @param name The name of the Device
     *
     * @throws NotFoundException If no device where found
     * @throws ClassCreationException If the class couldn't be created
     */
    public static <T extends Device> T getDeviceByName(Class<T> clazz, String name) throws NotFoundException,
            ClassCreationException {
        return createDeviceFromDbObject(clazz, queryDatabase("name", name));
    }

    /**
     * Gets a Device from the database using its name.
     *
     * @param name The name of the Device
     *
     * @throws NotFoundException If no device where found
     * @throws ClassCreationException If the class couldn't be created
     */
    public static Device getDeviceByName(String name) throws NotFoundException, ClassCreationException {
        return getDeviceByName(Device.class, name);
    }

    /**
     * @return The inner DBObject
     */
    public BasicDBObject getDbObj() {
        return obj;
    }

    /**
     * @return The unique id
     */
    public ObjectId getId() {
        return obj.getObjectId("_id");
    }

    /**
     * @return The name
     */
    public String getName() {
        return obj.getString("name");
    }

    /**
     * @param name The name to set
     *
     * @throws IllegalArgumentException If the name already is in use
     */
    public void setName(String name) throws IllegalArgumentException {
        if (queryDatabaseSafe("name", name) != null) {
            throw new IllegalArgumentException(String.format("The name '%s' is already in use", name));
        }
        obj.put("name", name);
    }

    /**
     * @return An array of types, ordered by position in tree
     */
    public abstract String[] getType();

    /**
     * Save to the database
     */
    public final void save() {
        Database.devices().save(obj);
    }
}
