package se.raxa.plugin.tellsticknet;

import com.mongodb.BasicDBObject;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;
import se.raxa.server.exceptions.MessageDeliveryException;
import se.raxa.server.exceptions.NotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * A service which handles all communication with the tellsticks.
 * initialize() should be called before anything else
 *
 * @author Rasmus Eneman
 */
public class TellstickNetService extends Thread {
    private static final int BUFFER_SIZE = 2048;
    private static final int TELLSTICK_LISTENING_PORT = 42314;
    private static final int TELLSTICK_SENDING_PORT = 30303;
    private static final int CACHE_UPDATE_DELAY = 15;
    private static final int CACHE_TIMEOUT_DELAY = (int)(CACHE_UPDATE_DELAY * 1.25) * (60 * 1000);

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    private static final ScheduledExecutorService SCHEDULE = Executors.newSingleThreadScheduledExecutor();

    private static TellstickNetService tellstickNetService = null;
    private DatagramSocket socket;

    private TellstickNetService() {}

    /**
     * Initializes the service by creating a socket, starting the listening thread and sends a discovery message
     *
     * @throws SocketException If the socket couldn't be created
     */
    public static void initialize() throws SocketException {
        tellstickNetService = new TellstickNetService();

        tellstickNetService.socket = new DatagramSocket(TELLSTICK_LISTENING_PORT);
        tellstickNetService.start();

        SCHEDULE.scheduleAtFixedRate(() -> {
            try {
                find();
            } catch (MessageDeliveryException e) {
                // Silently consume and live on
            }
            Tellsticks.removeOld();
        }, 0, CACHE_UPDATE_DELAY, TimeUnit.MINUTES);
    }

    /**
     * Called on a different thread
     * Listens and handles incoming message from tellsticks
     */
    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        String message;

        while (true) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                //TODO
            }

            message = new String(packet.getData());

            if (message.startsWith("TellStickNet")) {
                Tellsticks.put(message, packet.getAddress());
            }
        }
    }

    /**
     * @param packet DatagramPacket to send
     *
     * @throws MessageDeliveryException If the package somehow couldn't be sent
     */
    private static void send(DatagramPacket packet) throws MessageDeliveryException {
        try {
            tellstickNetService.socket.send(packet);
        } catch (IOException e) {
            throw new MessageDeliveryException(
                    String.format("Error when sending '%s' to '%s'",
                        new String(packet.getData()),
                        packet.getAddress().toString()
                    ),
                    e
            );
        }
    }

    /**
     * Sends a message to a specific Tellstick
     *
     * @param code The unique activation code of the Tellstick
     * @param message The message encoded as specified in http://developer.telldus.com/doxygen/html/TellStickNet.html
     */
    public static void send(String code, String message) {
        EXECUTOR.execute(() -> {
            try {
                InetAddress address = Tellsticks.get(code);
                byte[] buffer = message.getBytes("ISO-8859-1");

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, TELLSTICK_SENDING_PORT);
                send(packet);
            } catch (NotFoundException | UnsupportedEncodingException | MessageDeliveryException e) {
                // TODO
            }
        });
    }

    /**
     * Sends a discovery message
     *
     * @throws MessageDeliveryException If the message somehow couldn't be sent
     */
    public static void find() throws MessageDeliveryException {
        try {
            byte[] message = "D".getBytes("ISO-8859-1");

            DatagramPacket packet = new DatagramPacket(message, message.length,
                    InetAddress.getByName("255.255.255.255"), TELLSTICK_SENDING_PORT);
            send(packet);
        } catch (UnsupportedEncodingException | UnknownHostException e) {
            throw new MessageDeliveryException("Error when searching for tellsticks", e);
        }
    }

    /**
     * A cache for finding tellsticks on the network using it's activation code
     *
     * @author Rasmus Eneman
     */
    private static class Tellsticks {
        private static final int NUMBER_OF_TRIES = 20;
        private static final int TIME_FOR_RESPONSE = 150;

        private static final Map<String, Tellstick> tellsticks = new HashMap<>();
        private static final Pattern SEPARATOR = Pattern.compile(":");

        /**
         * Get the InetAddress of a tellstick. If not in the cache it will search the network
         *
         * @param code It's unique activation code
         *
         * @throws se.raxa.server.exceptions.NotFoundException If the tellstick can't be found
         */
        public static InetAddress get(String code) throws NotFoundException {
            InetAddress address;
            for (int i = 0; i < NUMBER_OF_TRIES; i++) {
                address = tellsticks.get(code).address;

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
            String[] pieces = SEPARATOR.split(identificationHeader);
            tellsticks.put(pieces[2], new Tellstick(address));

            BasicDBObject query = new BasicDBObject("type", "TellstickNet");
            query.put("code", pieces[2]);
            query = (BasicDBObject) Database.devices().findOne(query);
            if (query == null) {
                Device device = new TellstickNet();
                device.getDBObj().put("code", pieces[2]);
                device.getDBObj().put("version", pieces[3]);
                device.save();
            }
        }

        /**
         * Clear timed out tellsticks
         */
        public static void removeOld() {
            for (Map.Entry<String, Tellstick> entry : tellsticks.entrySet()) {
                if (entry.getValue().isOld()) {
                    tellsticks.remove(entry.getKey());
                }
            }
        }

        private static class Tellstick {
            private static final Date date = new Date();

            public InetAddress address;
            private final long time;

            public Tellstick(InetAddress address) {
                this.address = address;
                this.time = date.getTime();
            }

            /**
             * Check if this object have timed out and if so remove itself from the list
             */
            public boolean isOld() {
                return date.getTime() - time > CACHE_TIMEOUT_DELAY;
            }
        }
    }
}
