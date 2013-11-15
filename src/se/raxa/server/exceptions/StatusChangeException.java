package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class StatusChangeException extends Exception {
    public StatusChangeException(String message) {
        super(message);
    }

    public StatusChangeException(String message, Throwable t) {
        super(message, t);
    }
}
