package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ExecutionException;
import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public interface Lamp extends Output, Executable {

    /**
     * Called when a new object is created
     */
    default void onCreate() {}

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
     * @param action The action to execute
     *
     * @throws ExecutionException If the action could be executed
     */
    @Override
    public default void execute(Object action) throws ExecutionException {
        try {
            setStatus(Status.fromInt((Integer) action));
        } catch (StatusChangeException e) {
            throw new ExecutionException("action not supported", e);
        } catch (ClassCastException e) {
            throw new ExecutionException("action not supported");
        }
    }

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void turnOn() throws StatusChangeException;

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void turnOff() throws StatusChangeException;
}
