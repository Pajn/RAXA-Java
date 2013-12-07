package se.raxa.server.devices.helpers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;

/**
 * @author Rasmus Eneman
 */
public class Member<T extends Device> {
    private Class clazz;

    private DBObject dbObject;
    private T device = null;

    public Member(DBObject dbObject) throws NotFoundException, ClassCreationException {
        this.dbObject = dbObject;
        try {
            device = (T) Devices.getDeviceById((ObjectId) dbObject.get("id"));
        } catch (ClassCastException ex) {
            throw new ClassCreationException("Not supported member", ex);
        }
    }

    public Member(T device) {
        dbObject = new BasicDBObject();
        this.device = device;
    }

    public DBObject getDbObject() {
        return dbObject;
    }

    public T getDevice() {
        return device;
    }

    public Object get(String key) {
        return dbObject.get(key);
    }

    public void put(String key, Object object) {
        dbObject.put(key, object);
    }
}
