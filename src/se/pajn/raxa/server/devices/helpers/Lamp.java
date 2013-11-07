package se.pajn.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public abstract class Lamp extends Output {

    public abstract boolean isTurnedOn();
    public abstract void turnOn();
    public abstract void turnOff();
}
