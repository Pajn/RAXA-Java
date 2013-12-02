package se.raxa.server.devices.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
public enum Status {
    On(0),
    Off(1),
    Dim(2),
    DimLevel(3);

    private static Map<Integer, Status> statuses = new HashMap<Integer,Status>();
    private int value;

    static {
        for(int i = 0; i < values().length; i++) {
            statuses.put(values()[i].value, values()[i]);
        }
    }

    public static Status fromInt(int i) {
        return statuses.get(i);
    }

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
