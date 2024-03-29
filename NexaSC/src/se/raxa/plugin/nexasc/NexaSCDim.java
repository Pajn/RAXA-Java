package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.DimmableByTime;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public class NexaSCDim extends NexaSCOnOff implements DimmableByTime {

    /**
     * Called when dimming should be initiated
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    @Override
    public void startDimming() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Dim));
            getDBObj().put("status", Status.Dim.getValue());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }

    /**
     * Called when dimming should be stopped
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    @Override
    public void stopDimming() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Dim));
            getDBObj().put("status", Status.On.getValue());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }
}
