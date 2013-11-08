package se.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public interface DimmableByTime {

    /**
     * Called when dimming should be initiated
     */
    public abstract void StartDimming();

    /**
     * Called when dimming should be stopped
     */
    public abstract void StopDimming();
}
