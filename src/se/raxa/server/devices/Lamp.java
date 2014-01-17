package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.StatusChangeException;
import se.raxa.server.plugins.devices.AddAction;

/**
 * @author Rasmus Eneman
 */
public interface Lamp extends Output, Executable {

    /**
     * @return True if the lamp is turned on
     */
    public default boolean isTurnedOn() {
        return getDBObj().getInt("status") != Status.Off.getValue();
    }

    /**
     * @param status The status to set
     *
     * @throws StatusChangeException If the status isn't supported or there were an error when the status were set
     */
    public default void setStatus(Status status) throws StatusChangeException {
        switch (status) {
            case On:
                turnOn();
                break;
            case Off:
                turnOff();
                break;
            default:
                throw new StatusChangeException("Status not supported");
        }
    }

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void turnOn() throws StatusChangeException;

    @AddAction(name="lamp:on")
    public default void turnOnCaller() throws StatusChangeException {
        turnOn();
    }

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void turnOff() throws StatusChangeException;

    @AddAction(name="lamp:off")
    public default void turnOffCaller() throws StatusChangeException {
        turnOff();
    }
}
