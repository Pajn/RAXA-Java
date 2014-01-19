package se.raxa.server.devices;

import se.raxa.server.exceptions.StatusChangeException;
import se.raxa.server.plugins.devices.AddAction;
import se.raxa.server.plugins.devices.GetProperty;

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
     * @return The current dim level
     */
    public abstract int getDimLevel();

    /**
     * @return The current dim level
     */
    @GetProperty("dim_level_status")
    public default int getDimLevelProperty() {
        return getDimLevel();
    }

    /**
     * Set the dim level
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    public abstract void setDimLevel(int level) throws StatusChangeException;

    @AddAction(name="dim_level", arguments = "0")
    public default void setDimLevelCaller(int level) throws StatusChangeException {
        setDimLevel(level);
    }
}
