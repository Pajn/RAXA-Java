package se.raxa.server.devices;

import se.raxa.server.exceptions.ExecutionException;

/**
 * @author Rasmus Eneman
 */
public interface Executable extends Device {

    /**
     * @param action The action to execute
     *
     * @throws ExecutionException If the action could be executed
     */
    public abstract void execute(Object action) throws ExecutionException;
}
