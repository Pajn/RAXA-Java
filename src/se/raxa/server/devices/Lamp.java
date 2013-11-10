package se.raxa.server.devices;

import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public interface Lamp extends Output {

    /**
     * Called when a new object is created
     */
    default void onCreate() {}

    /**
     * @return True if the lamp is turned on
     */
    public abstract boolean isTurnedOn();

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException
     */
    public abstract void turnOn() throws StatusChangeException;

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException
     */
    public abstract void turnOff() throws StatusChangeException;
}
