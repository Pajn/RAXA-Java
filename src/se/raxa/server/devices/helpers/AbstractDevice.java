package se.raxa.server.devices.helpers;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;

import static se.raxa.server.devices.helpers.Devices.queryDatabaseSafe;

/**
 * @author Rasmus Eneman
 */
public abstract class AbstractDevice implements Device {
    private BasicDBObject dbObject;

    protected AbstractDevice() {
        dbObject = new BasicDBObject();
        dbObject.put("_id", new ObjectId());
        dbObject.put("type", getType());
    }

    /**
     * @return The inner DBObject
     */
    public BasicDBObject getDBObj() {
        return dbObject;
    }

    /**
     * @param dbObject The inner DBObject
     */
    public void setDBObj(BasicDBObject dbObject) {
        this.dbObject = dbObject;
    }

    /**
     * @return The unique id
     */
    public ObjectId getId() {
        return dbObject.getObjectId("_id");
    }

    /**
     * @return The name
     */
    public String getName() {
        return dbObject.getString("name");
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
        dbObject.put("name", name);
    }

    /**
     * @return An array of types, ordered by position in tree
     */
    public abstract String[] getType();

    /**
     * Save to the database
     */
    public final void save() {
        Database.devices().save(dbObject);
    }
}
