package se.pajn.raxa.server.devices;

import se.pajn.raxa.server.devices.helpers.DimableByLevel;
import se.pajn.raxa.server.devices.helpers.DimableByTime;

/**
 * @author Rasmus Eneman
 */
public class NexaSLDim extends NexaSLOnOff implements DimableByLevel, DimableByTime {

    @Override
    public String[] getType() {
        return new String[] {"NexaSLDim", "DimableByLevel", "DimableByTime", "Lamp", "Output"};
    }

    @Override
    public int getDimLevelMax() {
        return 15;
    }

    @Override
    public int getDimLevel() {
        return getDbObj().getInt("dim_level", 0);
    }

    @Override
    public void setDimLevel(int level) {
        //TODO
    }

    @Override
    public void StartDiming() {
        //TODO
    }

    @Override
    public void StopDiming() {
        //TODO
    }
}
