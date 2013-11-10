package se.raxa.plugin.nexasc;

import se.raxa.server.plugins.Plugin;

import static se.raxa.server.plugins.implementions.LampPlugins.registerPlugin;

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
        registerPlugin("NexaSCDim", NexaSCDim.class);
        registerPlugin("NexaSCOnOff", NexaSCOnOff.class);
    }
}
