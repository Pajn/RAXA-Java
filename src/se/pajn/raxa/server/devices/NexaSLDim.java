package se.pajn.raxa.server.devices;

import se.pajn.raxa.server.devices.helpers.DimmableByLevel;
import se.pajn.raxa.server.devices.helpers.DimmableByTime;

/**
 * @author Rasmus Eneman
 */
public class NexaSLDim extends NexaSLOnOff implements DimmableByLevel, DimmableByTime {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSLDim", "DimmableByLevel", "DimmableByTime", "Lamp", "Output"};
    }

    /**
     * @return The highest supported dim level
     */
    @Override
    public int getDimLevelMax() {
        return 15;
    }

    /**
     * @return The lowest current dim level
     */
    @Override
    public int getDimLevel() {
        return getDbObj().getInt("dim_level", 0);
    }

    /**
     * Set the dim level
     */
    @Override
    public void setDimLevel(int level) {
        //TODO
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
