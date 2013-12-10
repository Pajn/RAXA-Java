package se.raxa.server.devices.helpers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;
import se.raxa.server.plugins.implementions.DevicePlugins;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class Devices {

    /**
     * Create a Device object from a database object
     *
     * @param clazz The class of the Device
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public static <T extends Device> T createDeviceFromDbObject(Class<T> clazz, BasicDBObject obj) throws
            ClassCreationException {
        T device;
        try {
            device = clazz.getConstructor(BasicDBObject.class).newInstance(obj);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            try {
                device = clazz.getConstructor().newInstance();
                device.setDBObj(obj);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                throw new ClassCreationException(String.format("Error when creating Device from class '%s'", clazz.getCanonicalName()), e2);
            }
        }
        return device;
    }

    /**
     * Create a Device object from a database object
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public static Device createDeviceFromDbObject(BasicDBObject obj) throws ClassCreationException {
        String type = (String) ((BasicDBList) obj.get("type")).get(0);
        return createDeviceFromDbObject(DevicePlugins.getClasses().get(type), obj);
    }

    /**
     * Gets all Devices from the database implementing the specified type
     *
     * @param type The type of the Device
     * @param name The name of the Device
     *
     * @throws ClassCreationException
     */
    public static <T extends Device> T createDeviceOfType(Class<T> type, String name, Map<String, String> kwargs) throws
            ClassCreationException, IllegalArgumentException {
        try {
            T device = type.getConstructor().newInstance();
            device.setName(name);
            device.onCreate(kwargs);
            return device;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ClassCreationException("error", e); //TODO nice message
        }
    }

    /**
     * Gets all Devices from the database implementing the specified type
     *
     * @param type The type of the Device
     * @param name The name of the Device
     *
     * @throws ClassCreationException
     */
    public static Device createDeviceOfType(String type, String name, Map<String, String> kwargs) throws
            ClassCreationException, IllegalArgumentException {
        return createDeviceOfType(DevicePlugins.getClasses().get(type), name, kwargs);
    }

    /**
     * Queries the database for one device using
     *
     * @param key The key of the value
     * @param value The value to search for
     *
     * @return A BasicDBObject for that device
     */
    public static BasicDBObject queryDatabaseSafe(String key, Object value) {
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
     * @throws se.raxa.server.exceptions.NotFoundException If no device where found
     */
    public static BasicDBObject queryDatabase(String key, Object value) throws NotFoundException {
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
        BasicDBObject dbObject = queryDatabase("_id", id);

        Class<? extends Device> clazz = DevicePlugins.getClasses().get(((BasicDBList) dbObject.get("type")).get(0));

        return createDeviceFromDbObject(clazz, dbObject);
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
     * Gets all Devices from the database implementing the specified type
     *
     * @param type The type of the Devices
     *
     * @throws ClassCreationException
     */
    public static List<Device> getDevicesByType(String type) throws ClassCreationException {
        List<Device> devices = new ArrayList<>();

        DBObject query = new BasicDBObject();

        if (type != null && type.length() != 0) {
            query.put("type", type);
        }

        try (DBCursor cursor = Database.devices().find(query)) {
            for (DBObject dbObj : cursor) {
                devices.add(createDeviceFromDbObject((BasicDBObject) dbObj));
            }
        }

        return devices;
    }

    /**
     * Gets all Devices from the database implementing the specified type
     *
     * @param type The type of the Devices
     *
     * @throws ClassCreationException
     */
    public static <T extends Device> List<T> getDevicesByType(Class<T> type) throws ClassCreationException {
        List<T> devices = new ArrayList<>();

        DBObject query = new BasicDBObject("type", type.getSimpleName());

        try (DBCursor cursor = Database.devices().find(query)) {
            for (DBObject dbObj : cursor) {
                devices.add(createDeviceFromDbObject(type, (BasicDBObject) dbObj));
            }
        }

        return devices;
    }
}
