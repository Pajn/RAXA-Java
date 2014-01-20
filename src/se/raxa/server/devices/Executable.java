package se.raxa.server.devices;

import se.raxa.server.plugins.devices.Action;
import se.raxa.server.plugins.devices.DeviceClassDescriptor;
import se.raxa.server.plugins.devices.DeviceClasses;
import se.raxa.server.exceptions.ExecutionException;
import se.raxa.server.plugins.devices.GetProperty;

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

    /**
     * @return An array of supported actions
     */
    @GetProperty("actions")
    public default String[] getActions() { //todo how to best handle this?
        DeviceClassDescriptor descriptor = DeviceClasses.getDescriptor(getClass());
        String[] actions = new String[descriptor.getSupportedActions().size()];

        for (int i = 0; i < actions.length; i++) {
            actions[i] = descriptor.getSupportedActions().get(i).string;
        }

        return actions;
    }
}
