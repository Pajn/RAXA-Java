package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.BadPluginException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class DeviceClasses {
    private static final Map<String, DeviceClassDescriptor> deviceClasses = new HashMap<>();

    /**
     * @return A map of all Device plugin classes
     */
    public static DeviceClassDescriptor getDescriptor(Class<? extends Device> clazz) {
        return deviceClasses.get(clazz.getName());
    }

    /**
     * @param name The full name of a class
     *
     * @return The class
     */
    public static Class<? extends Device> getClass(String name) {
        try {
            return deviceClasses.get(name).getClazz();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Class doesn't exist", e);
        }
    }

    /**
     * Register a new Device plugin class
     *
     * @param deviceClass The class of the plugin
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public static <T extends Device> void registerDevice(Class<T> deviceClass) throws BadPluginException {
        deviceClasses.put(deviceClass.getName(), new DeviceClassDescriptor(deviceClass));
    }
}
