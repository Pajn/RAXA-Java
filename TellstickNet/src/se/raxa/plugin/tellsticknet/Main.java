package se.raxa.plugin.tellsticknet;

import se.raxa.server.plugins.Plugin;

import static se.raxa.server.devices.Connectors.registerPlugin;

/**
 * @author Rasmus Eneman
 */
public class Main implements Plugin {
    @Override
    public String getName() {
        return "TellstickNet";
    }

    @Override
    public void init() {
        registerPlugin("TellstickNet", TellstickNet.class);
    }
}
