package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class ExecutionException extends Exception {
    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable t) {
        super(message, t);
    }
}
