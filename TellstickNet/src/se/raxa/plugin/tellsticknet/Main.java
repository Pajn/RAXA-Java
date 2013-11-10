package se.raxa.plugin.tellsticknet;

import se.raxa.server.plugins.Plugin;

import java.net.SocketException;

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

        try {
            TellstickNetService.initialize();
        } catch (SocketException e) {
            //TODO
        }
    }
}
