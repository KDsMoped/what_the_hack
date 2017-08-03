package de.hsd.hacking.Data;

/**
 * This class represents a manager. Managers are used for a lot of classes in WTH to interact with game data.
 * Examples: {@link de.hsd.hacking.Data.Missions.Mission}, {@link de.hsd.hacking.Entities.Employees.Employee}
 * Managers are always singletons. Creating an instance should not fill the manager with default data.
 * Managers are able to create protobuf message objects for everything that needs to be saved/loaded.
 * @author Julian
 */
public abstract class Manager implements ProtobufHandler {

    public abstract void createInstance();
    public abstract void instance();

    public abstract void initSelf();
    public abstract void initReferences();

    public abstract void loadDefaultState();
    public abstract void loadState();
}
