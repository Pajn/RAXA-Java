package se.raxa.server.devices;

import se.raxa.server.plugins.devices.AddAction;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public interface DimmableByTime extends Lamp {

    /**
     * @return True if the lamp is dimming
     */
    public default boolean isDimming() {
        return getDBObj().getInt("status") == Status.Dim.ordinal();
    }

    /**
     * @param status The status to set
     *
     * @throws StatusChangeException If the status isn't supported or there were an error when the status were set
     */
    @Override
    public default void setStatus(Status status) throws StatusChangeException {
        switch (status) {
            case Dim:
                if (isDimming()) {
                    stopDimming();
                } else {
                    startDimming();
                }
                break;
            default:
                Lamp.super.setStatus(status);
        }
    }

    /**
     * Called when dimming should be initiated
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void startDimming() throws StatusChangeException;

    @AddAction(name="dim:start")
    public default void startDimmingCaller() throws StatusChangeException {
        startDimming();
    }

    /**
     * Called when dimming should be stopped
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void stopDimming() throws StatusChangeException;

    @AddAction(name="dim:stop")
    public default void stopDimmingCaller() throws StatusChangeException {
        stopDimming();
    }
}
