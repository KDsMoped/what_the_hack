package de.hsd.hacking.Data;

/**
 * This class represents a manager. Managers are used for a lot of classes in WTH to interact with game data.
 * Examples: {@link de.hsd.hacking.Data.Missions.Mission}, {@link de.hsd.hacking.Entities.Employees.Employee}
 * Managers are always singletons. Creating an instance should not fill the manager with default data.
 * Managers are able to create protobuf message objects for everything that needs to be saved/loaded.
 * @author Hendrik, Julian
 */
public interface Manager {

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    void initReferences();

    /**
     * Creates the default state of this manager when a new game is started.
     */
    void loadDefaultState();

    /**
     * Recreates the state this manager had before serialization.
     */
    void loadState();

    /**
     * Destroys manager this instance.
     */
    void cleanUp();
}
