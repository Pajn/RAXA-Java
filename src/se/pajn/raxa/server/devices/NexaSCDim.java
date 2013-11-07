package se.pajn.raxa.server.devices;

import se.pajn.raxa.server.devices.helpers.DimableByTime;

/**
 * @author Rasmus Eneman
 */
public class NexaSCDim extends NexaSCOnOff implements DimableByTime {

    @Override
    public String[] getType() {
        return new String[] {"NexaSCDim", "DimableByTime", "Lamp", "Output"};
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
