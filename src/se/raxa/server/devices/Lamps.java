package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Lamp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class Lamps {
    private static Map<String, Class<? extends Lamp>> lampPlugins = new HashMap<>();

    /**
     * @return A map of all lamp plugins
     */
    public static Map<String, Class<? extends Lamp>> getLampPlugins() {
        return lampPlugins;
    }

    /**
     * Register a new Lamp plugin
     *
     * @param name The identifying name of the plugin
     * @param lampClass The class of the plugin
     */
    public static <T extends Lamp> void registerPlugin(String name, Class<T> lampClass) {
        lampPlugins.put(name, lampClass);
    }
}
