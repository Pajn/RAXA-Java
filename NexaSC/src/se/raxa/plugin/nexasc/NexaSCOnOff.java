package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.helpers.Lamp;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

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
        getDbObj().put("device", device);
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
