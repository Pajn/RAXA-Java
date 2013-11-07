package se.pajn.raxa.server.devices;

import se.pajn.raxa.server.devices.helpers.Lamp;
import se.pajn.raxa.server.devices.helpers.Status;
import se.pajn.raxa.server.devices.interfaces.NexaSL;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOff extends Lamp implements NexaSL {

    @Override
    public String[] getType() {
        return new String[] {"NexaSLOnOff", "Lamp", "Output"};
    }

    @Override
    public long getSenderID() {
        return (byte) getDbObj().getLong("sender_id");
    }

    @Override
    public boolean isTurnedOn() {
        return getDbObj().getInt("status") != Status.Off.ordinal();
    }

    @Override
    public void turnOn() {
        //TODO
    }

    @Override
    public void turnOff() {
        //TODO
    }
}
