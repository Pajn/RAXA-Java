package se.raxa.plugin.nexasc;

import se.raxa.server.devices.helpers.Status;

/**
 * @author Rasmus Eneman
 */
public interface NexaSC {

    /**
     * @return The house code of the device
     */
    public byte getHouse();

    /**
     * @return The device code of the device
     */
    public byte getDevice();

    /**
     * @return An encoded code switch
     */
    default String getCodeSwitchTuple(byte intCode) {
        StringBuilder tuple = new StringBuilder();

        for( int i = 0; i < 4; ++i ) {
            if ((intCode & 1) != 0) {
                tuple.append("$kk$"); // 1
            } else {
                tuple.append("$k$k"); // 0
            }
            intCode >>= 1;
        }

        return tuple.toString();
    }

    /**
     * Encodes a message to be sent to a Tellstick
     *
     * @param status The status to send
     * @return A string ready to pass on to a Tellstick
     */
    public default String encodeMessage(Status status) {
        StringBuilder message = new StringBuilder();

        message.append(getCodeSwitchTuple(getHouse()));
        message.append(getCodeSwitchTuple(getDevice()));
        if (status == Status.Off) {
            message.append("$k$k$kk$$kk$$k$k$k");
        } else {
            message.append("$k$k$kk$$kk$$kk$$k");
        }

        return message.toString();
    }
}
