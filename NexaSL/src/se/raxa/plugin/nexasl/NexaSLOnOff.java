package se.raxa.plugin.nexasl;

import com.mongodb.BasicDBObject;
import se.raxa.plugin.tellsticknet.TellstickNet;
import se.raxa.server.Database;
import se.raxa.server.devices.helpers.Lamp;
import se.raxa.server.devices.helpers.Status;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

import java.util.Random;

/**
 * @author Rasmus Eneman
 */
public class NexaSLOnOff extends Lamp implements NexaSL {

    /**
     * Called when a new object is created
     * Generates a unique random id as sender id
     */
    @Override
    protected void onCreate() {
        Random r = new Random();
        int rand;
        BasicDBObject query;

        do {
            rand = r.nextInt(67234433);

            query = new BasicDBObject("type", "NexaSL");
            query.put("sender_id", rand);

        } while (Database.devices().findOne(query) != null);

        this.getDbObj().put("sender_id", rand);
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
        return getDbObj().getLong("sender_id");
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
