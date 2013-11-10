package se.raxa.plugin.tellsticknet;

import se.raxa.server.exceptions.MessageDeliveryException;
import se.raxa.server.exceptions.NotFoundException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * A cache for finding tellsticks on the network using it's activation code
 *
 * @author Rasmus Eneman
 */
public class Tellsticks {
    private static final int NUMBER_OF_TRIES = 20;
    private static final int TIME_FOR_RESPONSE = 150;

    private static Map<String, InetAddress> tellsticks = new HashMap<>();

    /**
     * Get the InetAddress of a tellstick. If not in the cache it will search the network
     *
     * @param code It's unique activation code
     *
     * @throws NotFoundException If the tellstick can't be found
     */
    public static InetAddress get(String code) throws NotFoundException {
        InetAddress address;
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            address = tellsticks.get(code);

            if (address != null) {
                return address;
            }

            try {
                TellstickNetService.find();
            } catch (MessageDeliveryException e) {
                // Silently consume and live on
            }

            try {
                Thread.sleep(TIME_FOR_RESPONSE);
            } catch (InterruptedException e) {
                // Silently consume and live on
            }
        }
        throw new NotFoundException("Tellstick not found");
    }

    /**
     * Put a tellstick on the cache
     *
     * @param identificationHeader The identification header provided by the tellstick
     * @param address The InetAddress to the tellstick
     */
    public static void put(String identificationHeader, InetAddress address) {
        String[] pieces = identificationHeader.split(":");
        tellsticks.put(pieces[2], address);
    }
}
