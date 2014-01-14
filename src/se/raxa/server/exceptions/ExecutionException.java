package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class ExecutionException extends Exception {
    public ExecutionException(Throwable t) {
        super(t);
    }
}
