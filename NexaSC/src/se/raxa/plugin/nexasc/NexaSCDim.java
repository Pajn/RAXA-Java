package se.raxa.plugin.nexasc;

import se.raxa.server.devices.helpers.DimmableByTime;

/**
 * @author Rasmus Eneman
 */
public class NexaSCDim extends NexaSCOnOff implements DimmableByTime {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSCDim", "DimmableByTime", "Lamp", "Output"};
    }

    /**
     * Called when dimming should be initiated
     */
    @Override
    public void StartDimming() {
        //TODO
    }

    /**
     * Called when dimming should be stopped
     */
    @Override
    public void StopDimming() {
        //TODO
    }
}
