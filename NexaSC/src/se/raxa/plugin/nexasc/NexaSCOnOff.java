package se.raxa.plugin.nexasc;

import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

import java.util.ArrayList;
import java.util.List;
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
        super.onCreate(kwargs);

        try {
            setHouse(Byte.parseByte(kwargs.get("house")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("house must be a integer between 0 and 15");
        }

        try {
            setDevice(Byte.parseByte(kwargs.get("device")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("device must be a integer between 0 and 15");
        }
    }

    /**
     * Called when the device is updated
     *
     * @param kwargs A map with arguments to update
     *
     * @throws IllegalArgumentException If at least one of the kwargs are invalid
     */
    public void onUpdate(Map<String, String> kwargs) throws IllegalArgumentException {
        super.onUpdate(kwargs);

        if (kwargs.containsKey("house")) {
            setHouse(Byte.parseByte(kwargs.get("house")));
        }

        if (kwargs.containsKey("device")) {
            setDevice(Byte.parseByte(kwargs.get("device")));
        }
    }

    /**
     * Called when the device should be presented
     *
     * @return A map with details that should be outputted
     */
    @Override
    public Map<String, Object> describe() {
        Map<String, Object> map = super.describe();

        map.put("house", getHouse());
        map.put("device", getDevice());

        return map;
    }

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
    public byte getHouse() {
        return (byte) getDBObj().getInt("house");
    }

    /**
     * Set the house code of the device
     * @param house The house code
     */
    public void setHouse(byte house) throws IllegalArgumentException {
        if (house < 0 || house > 15) {
            throw new IllegalArgumentException("house must be a integer between 0 and 15");
        }

        getDBObj().put("house", house);
    }

    /**
     * @return The device code of the device
     */
    @Override
    public byte getDevice() {
        return (byte) getDBObj().getInt("device");
    }

    /**
     * Set the device code of the device
     * @param device The device code
     */
    public void setDevice(byte device) throws IllegalArgumentException {
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
