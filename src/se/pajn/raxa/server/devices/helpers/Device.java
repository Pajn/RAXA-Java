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

    public BasicDBObject getDbObj() {
        return obj;
    }

    public ObjectId getId() {
        return obj.getObjectId("_id");
    }

    public String getName() {
        return obj.getString("name");
    }

    public void setName(String name) {
        obj.put("name", name);
    }

    public abstract String[] getType();

    public final void save() {
        Database.devices.save(obj);
    }
}
