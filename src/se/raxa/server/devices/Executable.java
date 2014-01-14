package se.raxa.server.devices;

import se.raxa.server.plugins.devices.Action;
import se.raxa.server.plugins.devices.DeviceClasses;
import se.raxa.server.exceptions.ExecutionException;

/**
 * @author Rasmus Eneman
 */
public interface Executable extends Device {

    /**
     * @param action The action to execute
     *
     * @throws IllegalArgumentException If the action isn't supported
     * @throws ExecutionException If the action could be executed
     */
    public default void execute(String action) throws ExecutionException {
        for (Action a : DeviceClasses.getDescriptor(getClass()).getSupportedActions()) {
            if (a.executeIfMatch(action, this)) {
                return;
            }
        }
        throw new IllegalArgumentException("Action not supported");
    }
}
