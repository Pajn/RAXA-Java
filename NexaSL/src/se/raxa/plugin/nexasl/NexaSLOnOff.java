package se.raxa.plugin.nexasl;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.helpers.Lamp;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOff extends Lamp implements NexaSL {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSLOnOff", "Lamp", "Output"};
    }

    /**
     * @return The unique sender id of the device
     */
    @Override
    public long getSenderID() {
        return (byte) getDbObj().getLong("sender_id");
    }

    /**
     * @return True if the lamp is turned on
     */
    @Override
    public boolean isTurnedOn() {
        return getDbObj().getInt("status") != Status.Off.ordinal();
    }

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException
     */
    @Override
    public void turnOn() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.On));
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException
     */
    @Override
    public void turnOff() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Off));
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }
}
