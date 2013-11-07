package se.pajn.raxa.server.devices.interfaces;

/**
 * @author Rasmus Eneman
 */
public interface NexaSC {

    /**
     * @return The house code of the device
     */
    public byte getHouse();

    /**
     * @return The device code of the device
     */
    public byte getDevice();
}
