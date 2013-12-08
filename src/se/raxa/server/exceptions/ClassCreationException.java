package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class ClassCreationException extends Exception {
    public ClassCreationException(String message) {
        super(message);
    }

    public ClassCreationException(String message, Throwable t) {
        super(message, t);
    }
}
