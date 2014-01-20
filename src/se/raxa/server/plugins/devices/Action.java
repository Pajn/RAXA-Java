package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Executable;
import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.exceptions.ExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author Rasmus Eneman
 */
public class Action {
    private static final Pattern COLON = Pattern.compile(":");
    private static final Pattern INTEGER = Pattern.compile("-?\\d+");

    private String name;
    private ActionType type;
    private Method method;

    public final String string;

    public Action(AddAction annotation, Method method) throws BadPluginException {
        name = annotation.name();
        this.method = method;

        if (name.contains(":") && method.getParameterCount() == 0) {
            string = name;
        } else if (!name.contains(":") && method.getParameterCount() == 1) {
            Class argumentClass = method.getParameterTypes()[0];
            if (argumentClass == int.class) {
                type = new Int(annotation.arguments());
            } else {
                throw new BadPluginException(String.format("An action of type \"%s\" is not supported", argumentClass.getSimpleName()));
            }
            string = String.format("%s:%s", name, type.toString());
        } else {
            throw new BadPluginException("An action can only receive one parameter if name doesn't contain : or zero parameters if i does");
        }
    }

    /**
     * Validate the action String
     *
     * @param action A string representing the action
     *
     * @return true if the string action matches this Action instance
     *
     * @throws IllegalArgumentException If the action isn't well formed
     */
    public boolean validate(String action) throws IllegalArgumentException {
        if (name.equals(action)) {
            return true;
        } else {
            try {
                String[] splitAction = COLON.split(action);
                return this.name.equals(splitAction[0]) && type.validate(splitAction[1]);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("action is not well formed", e);
            }
        }
    }

    /**
     * Execute an action on a Executable Device
     *
     * @param action A string representing the action to execute
     * @param object The object to execute the action on
     *
     * @return true if the string action matches this Action instance and the action have been executed
     *
     * @throws ExecutionException If the action method throws an Exception
     * @throws IllegalArgumentException If the action isn't well formed
     */
    public boolean executeIfMatch(String action, Executable object) throws ExecutionException, IllegalArgumentException {
        if (name.equals(action)) {
            try {
                method.invoke(object);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ExecutionException(e);
            }
        } else {
            try {
                String[] splitAction = COLON.split(action);
                return this.name.equals(splitAction[0]) && type.validateAndExecute(splitAction[1], object);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("action is not well formed", e);
            }
        }
    }

    private interface ActionType {
        boolean validate(String value);
        boolean validateAndExecute(String value, Executable object) throws ExecutionException;
    }

    private class Int implements ActionType {
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
        public boolean validate(String value) {
            if (!INTEGER.matcher(value).matches()) {
                return false;
            }
            Integer i = Integer.parseInt(value);

            return (maxValue != null && minValue <= i && i <= maxValue) ||
                    (maxValue == null && (minValue == null || i >= minValue));
        }

        @Override
        public boolean validateAndExecute(String value, Executable object) throws ExecutionException {
            if (validate(value)) {
                try {
                    method.invoke(object, Integer.parseInt(value));
                    return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ExecutionException(e);
                }
            }
            return false;
        }

        @Override
        public String toString() {
            if (maxValue != null) {
                return String.format("int:%d:%d", minValue, maxValue);
            } else if (minValue != null) {
                return String.format("int:%d", minValue);
            } else {
                return "int";
            }
        }
    }
}
