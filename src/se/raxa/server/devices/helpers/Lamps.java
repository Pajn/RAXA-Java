package se.raxa.server.devices.helpers;

import se.raxa.server.devices.Connector;
import se.raxa.server.devices.Lamp;
import se.raxa.server.exceptions.ClassCreationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class Lamps {

    /**
     * Create a new Lamp
     *
     * @param clazz Class of the Lamp
     * @param name Name of the Lamp
     * @param connector Connector of the lamp, may be null if appropriate
     *
     * @return A new Lamp object
     *
     * @throws se.raxa.server.exceptions.ClassCreationException
     */
    public static <T extends Lamp> T create(Class<T> clazz, String name, Connector connector) throws
            ClassCreationException {
        try {
            T obj = clazz.newInstance();
            obj.setName(name);
            obj.getDBObj().put("connector", connector.getDBObj());
            obj.onCreate();
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassCreationException(String.format("Error when creating lamp from '%s'", clazz.getSimpleName()), e);
        }
    }
}
