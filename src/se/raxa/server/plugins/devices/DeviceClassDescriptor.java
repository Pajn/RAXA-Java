package se.raxa.server.plugins.devices;

import org.apache.commons.lang3.ClassUtils;
import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.BadPluginException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Rasmus Eneman
 */
public class DeviceClassDescriptor {
    private Class<? extends Device> clazz;
    private String[] types;
    private List<Getter> getters = new ArrayList<>();
    private List<Setter> setters = new ArrayList<>();
    private List<Action> supportedActions = new ArrayList<>();

    public DeviceClassDescriptor(Class<? extends Device> clazz) throws BadPluginException {
        this.clazz = clazz;

        this.collectTypes();
        this.collectAnnotations();
    }

    private void collectTypes() {
        Collection<String> typeList = new ArrayList<>();
        typeList.add(clazz.getName());

        for (Class parent : ClassUtils.getAllInterfaces(clazz)) {
            if (Device.class.isAssignableFrom(parent)) {
                typeList.add(parent.getName());
            }
        }

        types = typeList.toArray(new String[typeList.size()]);
    }

    private void collectAnnotations() throws BadPluginException {
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
     * @return An array of types, ordered by rough position in tree
     */
    public String[] getTypes() {
        return types;
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

    /**
     * @param action A string representing the action
     *
     * @return true if action is supported
     */
    public boolean supportsAction(String action) {
        for (Action a : supportedActions) {
            if (a.validate(action)) {
                return true;
            }
        }
        return false;
    }
}
