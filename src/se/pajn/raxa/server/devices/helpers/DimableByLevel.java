package se.pajn.raxa.server.devices.helpers;

/**
 * @author Rasmus Eneman
 */
public interface DimableByLevel {

    public abstract int getDimLevelMax();
    public default int getDimLevelMin() {
        return 0;
    }

    public abstract int getDimLevel();
    public abstract void setDimLevel(int level);
}
