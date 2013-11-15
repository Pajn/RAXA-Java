package se.raxa.plugin.nexasl;

import com.mongodb.BasicDBObject;
import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.Database;
import se.raxa.server.devices.Lamp;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

import java.util.Random;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOff extends AbstractDevice implements Lamp, NexaSL {

    /**
     * Called when a new object is created
     * Generates a unique random id as sender id
     */
    @Override
    public void onCreate() {
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
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"NexaSLOnOff", "NexaSL", "Lamp", "Output"};
    }

    /**
     * @return The unique sender id of the device
     */
    @Override
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
            getDBObj().put("status", Status.On.ordinal());
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
            getDBObj().put("status", Status.Off.ordinal());
        } catch (ClassCreationException e) {
            throw new StatusChangeException("error when getting connector", e);
        }
    }
}
