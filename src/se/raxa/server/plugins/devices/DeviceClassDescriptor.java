package se.raxa.server.plugins.devices;

import se.raxa.server.devices.Device;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rasmus Eneman
 */
public class DeviceClassDescriptor {
    private Class<? extends Device> clazz;
    private List<Action> supportedActions = new ArrayList<>();

    public DeviceClassDescriptor(Class<? extends Device> clazz) {
        this.clazz = clazz;

        this.collectSupportedActions();
    }

    private void collectSupportedActions() {
        for (Method method : clazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof AddAction) {
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
     * @return a List with all Actions supported by the device
     */
    public List<Action> getSupportedActions() {
        return supportedActions;
    }
}
