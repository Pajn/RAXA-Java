package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Lamp;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.devices.interfaces.NexaSL;

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
     */
    @Override
    public void turnOn() {
        //TODO
    }

    /**
     * Called when the lamp should turn off
     */
    @Override
    public void turnOff() {
        //TODO
    }
}
