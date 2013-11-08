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
     * Gets a Device from the database using its id.
     *
     * @param clazz The class of the Device
     * @param id The MongoDB id of the Device
     *
     * @throws NotFoundException
     * @throws ClassCreationException
     */
    public static <T extends Device> T getDeviceById(Class<T> clazz, ObjectId id) throws NotFoundException,
            ClassCreationException {
        BasicDBObject query = new BasicDBObject("_id", id);
        query = (BasicDBObject) Database.devices().findOne(query);
        if (query == null) {
            throw new NotFoundException(String.format("No device with the id '%s' found", id.toString()));
        }
        return createDeviceFromDbObject(clazz, query);
    }

    /**
     * Gets a Device from the database using its id.
     *
     * @param id The MongoDB id of the Device
     *
     * @throws NotFoundException
     * @throws ClassCreationException
     */
    public static Device getDeviceById(ObjectId id) throws NotFoundException, ClassCreationException {
        return getDeviceById(Device.class, id);
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
     */
    public void setName(String name) {
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
