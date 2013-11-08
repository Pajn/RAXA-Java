package se.raxa.server;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * @author Rasmus Eneman
 */
public class Database {
    private static Database obj;

    private DB db;
    private DBCollection devices;

    private Database() {}

    public static void connect(MongoClient mongoClient) {
        if (obj == null) {
            obj = new Database();
            obj.db = mongoClient.getDB("RAXA");
            obj.devices = obj.db.getCollection("devices");
        } else {
            throw new ExceptionInInitializerError("Can only connect once");
        }
    }

    /**
     * @return Get the Mongo DB object
     */
    public static DB db() {
        return obj.db;
    }

    /**
     * @return Get the devices collection
     */
    public static DBCollection devices() {
        return obj.devices;
    }
}
