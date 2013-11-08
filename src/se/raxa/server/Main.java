package se.raxa.server;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

import static se.raxa.server.plugins.PluginLoader.initPlugins;

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

        initPlugins();
    }
}
