package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.BadPluginException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Rasmus Eneman
 */
public class Getter {
    private String name;
    private Method method;

    public Getter(GetProperty annotation, Method method) {
        name = annotation.value();
        this.method = method;
    }

    /**
     * @return The name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * @param object The device object to get the property from
     *
     * @return The value of the property
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public Object getValue(Device object) throws BadPluginException {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new BadPluginException("Exception when calling getter", e);
        }
    }
}
