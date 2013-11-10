package se.raxa.server.devices;

import se.raxa.server.devices.helpers.*;

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
}
