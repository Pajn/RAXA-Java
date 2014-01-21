package se.raxa.plugin.tellsticknet;

import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.plugins.Plugin;

import java.net.SocketException;

import static se.raxa.server.plugins.devices.DeviceClasses.registerDevice;

/**
 * @author Rasmus Eneman
 */
public class Main implements Plugin {
    @Override
    public String getName() {
        return "TellstickNet";
    }

    @Override
    public void init() throws BadPluginException {
        registerDevice(TellstickNet.class);

        try {
            TellstickNetService.initialize();
        } catch (SocketException e) {
            //TODO
        }
    }
}
