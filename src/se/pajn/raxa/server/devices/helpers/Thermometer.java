package se.pajn.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public abstract class Thermometer extends Sensor {

    /**
     * @return The resolution of the thermometer in degrees celsius
     */
    public abstract float getResolution();

    /**
     * @return The update frequency of the thermometer in seconds
     */
    public abstract float getUpdateFrequency();

    /**
     * @return The maximal supported temperature of the thermometer in degrees celsius
     */
    public abstract float getTemperatureMax();

    /**
     * @return The minimal supported temperature of the thermometer in degrees celsius
     */
    public abstract float getTemperatureMin();

    /**
     * @return The current or last measured temperature of the thermometer in degrees celsius
     */
    public abstract float getTemperature();
}
