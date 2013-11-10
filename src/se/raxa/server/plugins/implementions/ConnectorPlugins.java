package se.raxa.server.plugins.implementions;

import se.raxa.server.devices.Connector;
import se.raxa.server.devices.helpers.Devices;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class ConnectorPlugins {
    private static final Map<String, Class<? extends Connector>> connectorClasses = new HashMap<>();

    /**
     * @return A map of all Connector plugin classes
     */
    public static Map<String, Class<? extends Connector>> getClasses() {
        return connectorClasses;
    }

    /**
     * Register a new Connector plugin class
     *
     * @param name The identifying name of the plugin
     * @param connectorClass The class of the plugin
     */
    public static <T extends Connector> void registerPlugin(String name, Class<T> connectorClass) {
        DevicePlugins.registerPlugin(name, connectorClass);
        connectorClasses.put(name, connectorClass);
    }
}
