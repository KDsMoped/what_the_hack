package de.hsd.hacking.Data;

/**
 * @author Julian
 */
public interface DataContainer {
    /**
     * Creates a protobuf message object with data for a savegame.
     * @return Protobuf message object
     */
    com.google.protobuf.GeneratedMessageV3 getData();
}
