package se.raxa.server.devices.helpers;

import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.StatusChangeException;

/**
 * @author Rasmus Eneman
 */
public abstract class Lamp extends Output {

    /**
     * Create a new Lamp
     *
     * @param clazz Class of the Lamp
     * @param name Name of the Lamp
     * @param connector Connector of the lamp, may be null if appropriate
     *
     * @return A new Lamp object
     *
     * @throws ClassCreationException
     */
    public static <T extends Lamp> T create(Class<T> clazz, String name, Connector connector) throws
            ClassCreationException {
        try {
            T obj = clazz.newInstance();
            obj.setName(name);
            obj.getDbObj().put("connector", connector.getDbObj());
            obj.onCreate();
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassCreationException(String.format("Error when creating lamp from '%s'", clazz.getSimpleName()), e);
        }
    }

    /**
     * Called when a new object is created
     */
    protected void onCreate() {}

    /**
     * @return True if the lamp is turned on
     */
    public abstract boolean isTurnedOn();

    /**
     * Called when the lamp should turn on
     *
     * @throws StatusChangeException
     */
    public abstract void turnOn() throws StatusChangeException;

    /**
     * Called when the lamp should turn off
     *
     * @throws StatusChangeException
     */
    public abstract void turnOff() throws StatusChangeException;
}
