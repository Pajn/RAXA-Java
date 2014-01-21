package se.raxa.server.devices;

import se.raxa.server.plugins.devices.GetProperty;

/**
 * @author Rasmus Eneman
 */
public interface Connector extends Device {

    /**
     * @return False if it have wrong version or in any other way can't be used
     */
    public default boolean isUsable() {
        return true;
    }

    /**
     * @return False if it have wrong version or in any other way can't be used
     */
    @GetProperty("is_usable")
    public default boolean isUsableProperty() {
        return isUsable();
    }
}
