package se.raxa.plugin.nexasl;

import se.raxa.server.plugins.Plugin;

import static se.raxa.server.plugins.devices.DeviceClasses.registerDevice;

/**
 * @author Rasmus Eneman
 */
public class Main implements Plugin {
    @Override
    public String getName() {
        return "Nexa Self Learning";
    }

    @Override
    public void init() {
        registerDevice(NexaSLDim.class);
        registerDevice(NexaSLOnOff.class);
    }
}
