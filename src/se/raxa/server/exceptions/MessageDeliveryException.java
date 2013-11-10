package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class MessageDeliveryException extends Exception {
    public MessageDeliveryException(String message, Throwable t) {
        super(message, t);
    }
}
