package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

/**
 * @author Rasmus Eneman
 */
public interface Device {

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
     * @return The name
     */
    public abstract String getName();

    /**
     * @param name The name to set
     *
     * @throws IllegalArgumentException If the name already is in use
     */
    public abstract void setName(String name) throws IllegalArgumentException;

    /**
     * @return An array of types, ordered by position in tree
     */
    public abstract String[] getType();

    /**
     * Save to the database
     */
    public abstract void save();
}
