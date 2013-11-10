package se.raxa.server.devices;

import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public interface DimmableByTime extends Lamp {

    /**
     * Called when dimming should be initiated
     *
     * @throws StatusChangeException
     */
    public abstract void StartDimming() throws StatusChangeException;

    /**
     * Called when dimming should be stopped
     *
     * @throws StatusChangeException
     */
    public abstract void StopDimming() throws StatusChangeException;
}
