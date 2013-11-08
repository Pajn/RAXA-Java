package se.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public abstract class Connector extends Device {

    /**
     * @return False if it have wrong version or in any other way can't be used
     */
    public boolean isUsable() {
        return true;
    }
}
