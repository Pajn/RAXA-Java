package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Executable;
import se.raxa.server.exceptions.ExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author Rasmus Eneman
 */
public class Action {
    private static final Pattern COLON = Pattern.compile(":");
    private static final Pattern INTEGER = Pattern.compile("-?\\d+");

    String name;
    ActionType type;
    Method method;

    public Action(AddAction annotation, Method method) {
        name = annotation.name();
        this.method = method;

        if (!name.contains(":")) {
            Class argumentClass = method.getParameterTypes()[0];
            if (argumentClass == String[].class) {
                type = new StringArray(annotation.arguments());
            } else if (argumentClass == int.class) {
                type = new Int(annotation.arguments());
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
                return this.name.equals(splitAction[0]) && type.execute(splitAction[1], object);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("action is not well formed", e);
            }
        }
    }

    private interface ActionType {
        boolean execute(String value, Executable object) throws ExecutionException;
    }

    private class StringArray implements ActionType {
        String[] keys;

        public StringArray(String[] arguments) {
            keys = arguments;
        }

        public boolean execute(String value, Executable object) throws ExecutionException {
            if (Arrays.asList(keys).contains(value)) {
                try {
                    method.invoke(object, value);
                    return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ExecutionException(e);
                }
            }
            return false;
        }
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

        public boolean execute(String value, Executable object) throws ExecutionException {
            if (!INTEGER.matcher(value).matches()) {
                return false;
            }
            Integer i = Integer.parseInt(value);

            if ((maxValue != null && minValue <= i && i <= maxValue) ||
                (maxValue == null && (minValue == null || i >= minValue))) {
                try {
                    method.invoke(object, i);
                    return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ExecutionException(e);
                }
            }
            return false;
        }
    }
}
