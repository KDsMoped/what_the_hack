package de.hsd.hacking.Data;

/**
 * A generic event listener interface.
 * @author Julian
 */
public interface EventListener {
    /**
     * These are the different event types we use.
     */
    enum EventType {
        MISSION_STARTED, MISSION_FINISHED, MISSION_ABORTED, MESSAGE_NEW, SAVE, LOAD
    }

    /**
     * Gets called when the event is fired.
     * @param type Type of the fired event.
     * @param sender Object that fired the event.
     */
    void OnEvent(EventType type, Object sender);
}