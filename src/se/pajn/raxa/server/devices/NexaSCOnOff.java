package se.pajn.raxa.server.devices;

import se.pajn.raxa.server.devices.helpers.Lamp;
import se.pajn.raxa.server.devices.helpers.Status;
import se.pajn.raxa.server.devices.interfaces.NexaSC;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOff extends Lamp implements NexaSC {

    @Override
    public String[] getType() {
        return new String[] {"NexaSCOnOff", "Lamp", "Output"};
    }

    @Override
    public byte getHouse() {
        return (byte) getDbObj().getInt("house");
    }

    @Override
    public byte getDevice() {
        return (byte) getDbObj().getInt("device");
    }

    public void setHouseDevice(byte house, byte device) {
        getDbObj().put("house", house);
        getDbObj().put("device", house);
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
