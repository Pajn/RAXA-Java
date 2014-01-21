package se.raxa.plugin.nexasl;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.DimmableByLevel;
import se.raxa.server.devices.DimmableByTime;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;
import se.raxa.server.plugins.devices.AddAction;

/**
 * @author Rasmus Eneman
 */
public class NexaSLDim extends NexaSLOnOff implements DimmableByLevel, DimmableByTime {

    /**
     * @return The lowest current dim level
     */
    @Override
    public int getDimLevel() {
        return getDBObj().getInt("dim_level", 0);
    }

    /**
     * Set the dim level
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    @Override
    public void setDimLevel(int level) throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.DimLevel, (byte) level));
            getDBObj().put("status", Status.On.getValue());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }

    @Override
    @AddAction(name="dim_level", arguments = {"0", "15"})
    public void setDimLevelCaller(int level) throws StatusChangeException {
        setDimLevel(level);
    }

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
