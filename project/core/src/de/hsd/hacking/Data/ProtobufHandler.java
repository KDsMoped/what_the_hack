package de.hsd.hacking.Data;


/**
 * This interface is used for all the Manager classes like
 * {@link de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager} or
 * {@link de.hsd.hacking.Data.Missions.MissionManager} to save and load persistent data.
 * @author Julian
 */
public interface ProtobufHandler {
    /**
     * Create a Protobuf object to save to disk.
     * @return protobuf message to be saved.
     */
    com.google.protobuf.GeneratedMessageV3 Save();
}
