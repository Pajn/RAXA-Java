package se.pajn.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public abstract class Thermometer extends Sensor {

    public abstract int getResolution();
    public abstract int getUpdateFrequency();
    public abstract int getTemperatureMax();
    public abstract int getTemperatureMin();

    public abstract int getTemperature();
}
