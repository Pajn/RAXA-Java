package se.raxa.server.devices;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public interface Device {

    /**
     * Called when a new object is created
     *
     * @param kwargs A map with arguments for creation
     *
     * @throws ClassCreationException If the class somehow can't be created
     * @throws IllegalArgumentException If at least one of the kwargs are invalid (missing or illegal value)
     */
    public default void onCreate(Map<String, String> kwargs) throws ClassCreationException, IllegalArgumentException {}

    /**
     * Called when the device is updated
     *
     * @param kwargs A map with arguments to update
     *
     * @throws IllegalArgumentException If at least one of the kwargs are invalid
     */
    public default void onUpdate(Map<String, String> kwargs) throws IllegalArgumentException {
        if (kwargs.containsKey("name")) {
            setName(kwargs.get("name"));
        }
    }

    /**
     * Called when the device should be presented
     *
     * @return A map with details that should be outputted
     */
    public default Map<String, Object> describe() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", getId().toStringMongod());
        map.put("name", getName());
        map.put("type", getType());

        return map;
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
