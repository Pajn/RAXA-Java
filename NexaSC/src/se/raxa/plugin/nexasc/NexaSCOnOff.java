package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;
import se.raxa.server.plugins.devices.GetProperty;
import se.raxa.server.plugins.devices.SetProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rasmus Eneman
 */
public class NexaSCOnOff extends AbstractDevice implements Lamp, NexaSC {

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSCOnOff", "Lamp", "Executable", "Output"};
    }

    /**
     * @return A list of supported Connector classes, contains null if supports not having one
     */
    @Override
    public List<Class> getSupportedConnectors() {
        List <Class> classes = new ArrayList<>();

        classes.add(TellstickNet.class);

        return classes;
    }

    /**
     * @return The house code of the device
     */
    @Override
    @GetProperty("house_code")
    public byte getHouse() {
        return (byte) getDBObj().getInt("house");
    }

    /**
     * Set the house code of the device
     * @param house The house code
     */
    @SetProperty("house_code")
    public void setHouse(int house) throws IllegalArgumentException {
        if (house < 0 || house > 15) {
            throw new IllegalArgumentException("house must be a integer between 0 and 15");
        }

        getDBObj().put("house", house);
    }

    /**
     * @return The device code of the device
     */
    @Override
    @GetProperty("device_code")
    public byte getDevice() {
        return (byte) getDBObj().getInt("device");
    }

    /**
     * Set the device code of the device
     * @param device The device code
     */
    @SetProperty("device_code")
    public void setDevice(int device) throws IllegalArgumentException {
        if (device < 0 || device > 15) {
            throw new IllegalArgumentException("device must be a integer between 0 and 15");
        }

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
