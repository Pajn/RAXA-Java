package se.raxa.server.plugins.implementions;

import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.Devices;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class LampPlugins {
    private static final Map<String, Class<? extends Lamp>> lampClasses = new HashMap<>();

    /**
     * @return A map of all lamp plugin classes
     */
    public static Map<String, Class<? extends Lamp>> getClasses() {
        return lampClasses;
    }

    /**
     * Register a new Lamp plugin class
     *
     * @param name The identifying name of the plugin
     * @param lampClass The class of the plugin
     */
    public static <T extends Lamp> void registerPlugin(String name, Class<T> lampClass) {
        DevicePlugins.registerPlugin(name, lampClass);
        lampClasses.put(name, lampClass);
    }
}
