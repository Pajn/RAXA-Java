package se.pajn.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public abstract class Lamp extends Output {

    /**
     * @return True if the lamp is turned on
     */
    public abstract boolean isTurnedOn();

    /**
     * Called when the lamp should turn on
     */
    public abstract void turnOn();

    /**
     * Called when the lamp should turn off
     */
    public abstract void turnOff();
}
