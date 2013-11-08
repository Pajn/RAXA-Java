package se.raxa.server;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * @author Rasmus Eneman
 */
public class Main {
    public static void main(String[] args) {
        try {
            Database.connect(new MongoClient());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
