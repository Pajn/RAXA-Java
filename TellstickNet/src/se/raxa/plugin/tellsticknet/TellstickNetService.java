package se.raxa.plugin.tellsticknet;

import se.raxa.server.exceptions.MessageDeliveryException;
import se.raxa.server.exceptions.NotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

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

        try {
            find();
        } catch (MessageDeliveryException e) {
            // Silently consume and live on
        }
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

            if (message.startsWith("TellstickNet")) {
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
}
