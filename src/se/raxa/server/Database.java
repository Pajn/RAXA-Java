package se.raxa.server;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * @author Rasmus Eneman
 */
public class Database {
    public static DB db;
    public static DBCollection devices;

    public static void connect(MongoClient mongoClient) {
        db = mongoClient.getDB("RAXA");
        devices = db.getCollection("devices");
    }
}
