package se.raxa.plugin.nexasl;

import se.raxa.server.plugins.Plugin;

import static se.raxa.server.plugins.implementions.LampPlugins.registerPlugin;

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
        registerPlugin("NexaSLDim", NexaSLDim.class);
        registerPlugin("NexaSLOnOff", NexaSLOnOff.class);
    }
}
