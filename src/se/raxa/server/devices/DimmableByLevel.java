package se.raxa.server.devices;

import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public interface DimmableByLevel extends Lamp {

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
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void setDimLevel(int level) throws StatusChangeException;
}
