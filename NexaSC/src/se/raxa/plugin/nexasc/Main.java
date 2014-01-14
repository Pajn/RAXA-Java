package se.raxa.plugin.nexasc;

import se.raxa.server.plugins.Plugin;

import static se.raxa.server.plugins.devices.DeviceClasses.registerDevice;

/**
 * @author Rasmus Eneman
 */
public class Main implements Plugin {
    @Override
    public String getName() {
        return "Nexa Switch Case";
    }

    @Override
    public void init() {
        registerDevice(NexaSCDim.class);
        registerDevice(NexaSCOnOff.class);
    }
}
