package se.raxa.server.devices;

import se.raxa.server.devices.helpers.Lamp;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.devices.interfaces.NexaSC;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOff extends Lamp implements NexaSC {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSCOnOff", "Lamp", "Output"};
    }

    /**
     * @return The house code of the device
     */
    @Override
    public byte getHouse() {
        return (byte) getDbObj().getInt("house");
    }

    /**
     * @return The device code of the device
     */
    @Override
    public byte getDevice() {
        return (byte) getDbObj().getInt("device");
    }

    /**
     * Set the house and device code of the device
     * @param house The house code
     * @param device The device code
     */
    public void setHouseDevice(byte house, byte device) {
        getDbObj().put("house", house);
        getDbObj().put("device", house);
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
