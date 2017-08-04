package de.hsd.hacking.Data;


/**
 * Generic event sender interface.
 * @author Julian
 */
public interface EventSender {

    /**
     * Add a listener to the list.
     * @param listener that wants to listen.
     */
    void addListener(EventListener listener);

    /**
     * Remove a listener from the list.
     * @param listener to be removed.
     */
    void removeListener(EventListener listener);

    /**
     * Fire an event with an specific type.
     * @param type event type.
     */
    void notifyListeners(EventListener.EventType type);
}
