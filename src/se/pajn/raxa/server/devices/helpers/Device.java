package se.pajn.raxa.server.devices.helpers;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.pajn.raxa.server.Database;

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
        Database.devices.save(obj);
    }
}
