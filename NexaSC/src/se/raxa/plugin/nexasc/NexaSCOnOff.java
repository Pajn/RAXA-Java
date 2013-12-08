package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOff extends AbstractDevice implements Lamp, NexaSC {

    /**
     * Called when a new object is created
     *
     * @param kwargs A map with arguments for creation
     *
     * @throws ClassCreationException If the class somehow can't be created
     * @throws IllegalArgumentException If at least one of the kwargs are invalid (missing or illegal value)
     */
    @Override
    public void onCreate(Map<String, String> kwargs) throws ClassCreationException, IllegalArgumentException {
        Lamp.super.onCreate(kwargs);

        byte house;
        try {
            house = Byte.parseByte(kwargs.get("house"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("house must be a integer between 0 and 15");
        }

        byte device;
        try {
            device = Byte.parseByte(kwargs.get("device"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("device must be a integer between 0 and 15");
        }

        setHouseDevice(house, device);
    }

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSCOnOff", "Lamp", "Executable", "Output"};
    }

    /**
     * @return The house code of the device
     */
    @Override
    public byte getHouse() {
        return (byte) getDBObj().getInt("house");
    }

    /**
     * @return The device code of the device
     */
    @Override
    public byte getDevice() {
        return (byte) getDBObj().getInt("device");
    }

    /**
     * Set the house and device code of the device
     * @param house The house code
     * @param device The device code
     */
    public void setHouseDevice(byte house, byte device) throws IllegalArgumentException {
        if (house < 0 || house > 15) {
            throw new IllegalArgumentException("house must be a integer between 0 and 15");
        }
        if (device < 0 || device > 15) {
            throw new IllegalArgumentException("device must be a integer between 0 and 15");
        }

        getDBObj().put("house", house);
        getDBObj().put("device", device);
    }

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    @Override
    public void turnOn() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.On));
            getDBObj().put("status", Status.On.getValue());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException If an error occurred (Example: Couldn't reach device or connector)
     */
    @Override
    public void turnOff() throws StatusChangeException {
        try {
            getConnector(TellstickNet.class).send(encodeMessage(Status.Off));
            getDBObj().put("status", Status.Off.getValue());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }
}
