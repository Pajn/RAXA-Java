package se.raxa.plugin.nexasl;

import se.raxa.server.devices.helpers.Status;

/**
 * @author Rasmus Eneman
 */
public interface NexaSL {
    static final String ONE = String.format("%c%c", (char) 29, (char) 17);
    static final String ZERO = String.format("%c%c", (char) 29, (char) 92);
    static final int key = 7;
    int UNIQUE_SENDER_ID_MAX = 67234433;

    /**
     * @return The unique sender id of the device
     */
    public long getSenderID();

    /**
     * Encodes a message to be sent to a Tellstick
     *
     * @param status The status to send
     * @param dimLevel The dim level (require Status.DimLevel)
     * @return A string ready to pass on to a Tellstick
     */
    public default String encodeMessage(Status status, byte dimLevel) {
        StringBuilder message = new StringBuilder();

        long senderID = getSenderID();

        for (int i = 25; i >= 0; --i) {
            if ((senderID & 1 << i) != 0) {
                message.append(ONE).append(ZERO);
            } else {
                message.append(ZERO).append(ONE);
            }
        }

        message.append(ZERO).append(ONE);

        switch (status) {
            case On:
            case Dim:
                message.append(ONE).append(ZERO);
                break;
            case Off:
                message.append(ZERO).append(ONE);
                break;
            case DimLevel:
                message.append(ZERO).append(ZERO);
                break;
        }

        for (int i = 3; i >= 0; --i) {
            if ((key & 1 << i) != 0) {
                message.append(ONE).append(ZERO);
            } else {
                message.append(ZERO).append(ONE);
            }
        }

        if (status == Status.DimLevel) {
            for (int i = 3; i >= 0; --i) {
                if ((dimLevel & 1 << i) != 0) {
                    message.append(ONE).append(ZERO);
                } else {
                    message.append(ZERO).append(ONE);
                }
            }
        }

        message.append(ZERO);

        return message.toString();
    }

    /**
     * Encodes a message to be sent to a Tellstick
     *
     * @param status The status to send
     * @return A string ready to pass on to a Tellstick
     */
    public default String encodeMessage(Status status) {
        return encodeMessage(status, (byte) 0);
    }
}
