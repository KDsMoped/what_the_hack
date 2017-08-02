package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic event sender interface.
 * @author Julian
 */
public interface EventSender {
    List<EventListener> listeners = new ArrayList<EventListener>();

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
