package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.BadPluginException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author Rasmus Eneman
 */
public class Setter {
    private static final Pattern INTEGER = Pattern.compile("-?\\d+");

    private String name;
    private boolean required;
    private Type type;
    private Method method;

    public Setter(SetProperty annotation, Method method) throws BadPluginException {
        name = annotation.value();
        required = annotation.required();
        this.method = method;

        Class argumentClass = method.getParameterTypes()[0];
        try {
            if (argumentClass == String.class) {
                type = new Str(annotation.arguments());
            } else if (argumentClass == int.class) {
                type = new Int(annotation.arguments());
            } else {
                throw new BadPluginException(String.format("A setter of type \"%s\" is not supported", argumentClass.getSimpleName()));
            }
        } catch (IllegalArgumentException e) {
            throw new BadPluginException(e.getMessage(), e);
        }
    }

    /**
     * @return The name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * @return True if this property is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param object The device object to set the property on
     * @param value The value to set on the property
     *
     * @throws BadPluginException If the plugin doesn't handle as expected
     * @throws IllegalArgumentException If the provided value isn't legal
     */
    public void setValue(Device object, String value) throws BadPluginException, IllegalArgumentException {
        type.setValue(object, value);
    }

    private interface Type {
        void setValue(Device object, String value) throws BadPluginException;
    }

    private class Str implements Type {
        public Str(String[] arguments) {
        }

        @Override
        public void setValue(Device object, String value) throws BadPluginException {
            if (value != null) {
                try {
                    method.invoke(object, value);
                } catch (IllegalAccessException e) {
                    throw new BadPluginException("Can't access setter", e);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof IllegalArgumentException) {
                        throw (IllegalArgumentException) e.getCause();
                    } else {
                        throw new BadPluginException("Other exception that IllegalArgumentException when calling setter", e);
                    }
                }
            }
        }
    }

    private class Int implements Type {
        Integer minValue;
        Integer maxValue;

        public Int(String[] arguments) {
            switch (arguments.length) {
                case 0:
                    break;

                case 1:
                    minValue = Integer.parseInt(arguments[0]);
                    break;

                case 2:
                    minValue = Integer.parseInt(arguments[0]);
                    maxValue = Integer.parseInt(arguments[1]);
                    break;

                default:
                    throw new IllegalArgumentException("0, 1 or 2 arguments required");
            }
        }

        @Override
        public void setValue(Device object, String value) throws BadPluginException {
            if (!INTEGER.matcher(value).matches()) {
                throw new IllegalArgumentException(String.format("Property \"%s\" require an integer", name));
            }
            Integer i = Integer.parseInt(value);

            if ((maxValue != null && minValue <= i && i <= maxValue) ||
                    (maxValue == null && (minValue == null || i >= minValue))) {
                try {
                    method.invoke(object, i);
                } catch (IllegalAccessException e) {
                    throw new BadPluginException("Can't access setter", e);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof IllegalArgumentException) {
                        throw (IllegalArgumentException) e.getCause();
                    } else {
                        throw new BadPluginException("Other exception that IllegalArgumentException when calling setter", e);
                    }
                }
            }
        }
    }
}
