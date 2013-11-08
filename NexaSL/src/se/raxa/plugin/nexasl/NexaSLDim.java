package se.raxa.plugin.nexasl;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.helpers.DimmableByLevel;
import se.raxa.server.devices.helpers.DimmableByTime;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

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
     *
     * @throws StatusChangeException
     */
    @Override
    public void setDimLevel(int level) throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.DimLevel, (byte) level));
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
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
