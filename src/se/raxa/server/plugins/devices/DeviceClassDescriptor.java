package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.BadPluginException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public class DeviceClassDescriptor {
    private Class<? extends Device> clazz;
    private List<Getter> getters = new ArrayList<>();
    private List<Setter> setters = new ArrayList<>();
    private List<Action> supportedActions = new ArrayList<>();

    public DeviceClassDescriptor(Class<? extends Device> clazz) throws BadPluginException {
        this.clazz = clazz;

        this.collectSupportedActions();
    }

    private void collectSupportedActions() throws BadPluginException {
        for (Method method : clazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof GetProperty) {
                    getters.add(new Getter(((GetProperty) annotation), method));
                } else if (annotation instanceof SetProperty) {
                    setters.add(new Setter(((SetProperty) annotation), method));
                } else if (annotation instanceof AddAction) {
                    supportedActions.add(new Action(((AddAction) annotation), method));
                }
            }
        }
    }

    /**
     * @return The class described
     */
    public Class<? extends Device> getClazz() {
        return clazz;
    }

    /**
     * @param object The device object to get properties from
     *
     * @return a Map with all property values on the device
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     */
    public Map<String, Object> getProperties(Device object) throws BadPluginException {
        Map<String, Object> properties = new HashMap<>();

        for (Getter getter : getters) {
            properties.put(getter.getName(), getter.getValue(object));
        }

        return properties;
    }

    /**
     * @param object The device object to set properties on
     * @param propertyValues a Map with all property values to set on the device
     * @param isNew True if the device is about to be created and required parameter should be honored
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     * @throws IllegalArgumentException if a property have an invalid value or
     *                                  if a required property isn't specified
     */
    public void setProperties(Device object, Map<String, String> propertyValues, boolean isNew) throws BadPluginException,
            IllegalArgumentException {
        for (Setter setter : setters) {
            if (propertyValues.containsKey(setter.getName())) {
                setter.setValue(object, propertyValues.get(setter.getName()));
            } else if (setter.isRequired() && isNew) {
                throw new IllegalArgumentException(String.format("Property \"%s\" is required but not provided",
                                                                 setter.getName()));
            }
        }
    }

    /**
     * @return a List with all Actions supported by the device
     */
    public List<Action> getSupportedActions() {
        return supportedActions;
    }
}
