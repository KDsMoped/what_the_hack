package de.hsd.hacking.Data;


/**
 * Generic event sender interface.
 * @author Julian
 */
public interface EventSender {

    /**
     * Add a listener to the list.
     * @param listener
     */
    void addListener(EventListener listener);

    /**
     * Remove a listener from the list.
     * @param listener
     */
    void removeListener(EventListener listener);

    /**
     * Fire an event with an specific type.
     * @param type
     */
    void notifyListeners(EventListener.EventType type);
}
