package se.raxa.server.exceptions;

/**
 * @author Rasmus Eneman
 */
public class BadPluginException extends Exception {
    public BadPluginException(String message) {
        super(message);
    }

    public BadPluginException(String message, Throwable t) {
        super(message, t);
    }
}
