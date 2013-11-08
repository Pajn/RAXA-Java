package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.helpers.DimmableByTime;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

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
     *
     * @throws StatusChangeException
     */
    @Override
    public void StartDimming() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Dim));
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }

    /**
     * Called when dimming should be stopped
     *
     * @throws StatusChangeException
     */
    @Override
    public void StopDimming() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Dim));
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }
}
