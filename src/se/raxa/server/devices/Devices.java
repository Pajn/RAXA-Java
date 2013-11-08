package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Device;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class Devices {
    private static Map<String, Class<? extends Device>> deviceClasses = new HashMap<>();

    /**
     * @return A map of all Device plugin classes
     */
    public static Map<String, Class<? extends Device>> getClasses() {
        return deviceClasses;
    }

    /**
     * Register a new Device plugin class
     *
     * @param name The identifying name of the plugin
     * @param deviceClass The class of the plugin
     */
    public static <T extends Device> void registerPlugin(String name, Class<T> deviceClass) {
        deviceClasses.put(name, deviceClass);
    }
}
