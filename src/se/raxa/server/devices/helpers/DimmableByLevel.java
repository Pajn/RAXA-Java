package se.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public interface DimmableByLevel {

    /**
     * @return The highest supported dim level
     */
    public abstract int getDimLevelMax();

    /**
     * @return The lowest supported dim level, defaults to 0
     */
    public default int getDimLevelMin() {
        return 0;
    }

    /**
     * @return The lowest current dim level
     */
    public abstract int getDimLevel();

    /**
     * Set the dim level
     */
    public abstract void setDimLevel(int level);
}
