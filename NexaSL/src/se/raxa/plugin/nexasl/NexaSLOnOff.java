package se.raxa.plugin.nexasl;

import com.mongodb.BasicDBObject;
import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.Database;
import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;
import se.raxa.server.plugins.devices.GetProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOff extends AbstractDevice implements Lamp, NexaSL {

    /**
     * Called when the device is about to be created
     *
     * @param propertyValues
     *         A map with values for all properties to be set
     *
     * @throws IllegalArgumentException
     *         if a property have an invalid value or if a required property isn't specified
     * @throws ClassCreationException If the device can't be created
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    @Override
    public void create(Map<String, String> propertyValues) throws IllegalArgumentException, ClassCreationException,
            BadPluginException {
        super.create(propertyValues);

        Random r = new Random();
        int rand;
        BasicDBObject query;

        do {
            rand = r.nextInt(UNIQUE_SENDER_ID_MAX);

            query = new BasicDBObject("type", "NexaSL");
            query.put("sender_id", rand);

        } while (Database.devices().findOne(query) != null);

        this.getDBObj().put("sender_id", rand);
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
     * @return The unique sender id of the device
     */
    @Override
    @GetProperty("sender_id")
    public long getSenderID() {
        return getDBObj().getLong("sender_id");
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
