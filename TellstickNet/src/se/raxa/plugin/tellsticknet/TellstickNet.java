package se.raxa.plugin.tellsticknet;

import se.raxa.server.devices.Connector;
import se.raxa.server.devices.helpers.AbstractDevice;

/**
 * @author Rasmus Eneman
 */
public class TellstickNet extends AbstractDevice implements Connector {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"TellstickNet", "Connector"};
    }

    /**
     * @return The unique activation code
     */
    public String getCode() {
        return getDBObj().getString("code");
    }

    /**
     * @return The firmware version
     */
    public String getVersion() {
        return getDBObj().getString("version");
    }

    /**
     * @return True if it have an usable version
     */
    @Override
    public boolean isUsable() {
        return getVersion().startsWith("RAXA");
    }

    /**
     * Send a RF message
     *
     * @param message The message in pulses as defined by http://developer.telldus.com/doxygen/html/TellStick.html#sec_send
     * @param repeats The number of repeats
     * @param delay The delay between repeats in ms
     */
    public void send(String message, int repeats, int delay) {
        message = String.format("4:sendh1:S%X:%s1:Pi%Xs1:Ri%Xss", message.length(), message, delay, repeats);

        TellstickNetService.send(getCode(), message);
    }


    /**
     * Send a RF message
     *
     * @param message The message in pulses as defined by http://developer.telldus.com/doxygen/html/TellStick.html#sec_send
     * repeats default to 5
     * delay defaults to 10ms
     */
    public void send(String message) {
        send(message, 5, 10);
    }
}
