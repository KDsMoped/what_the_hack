package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ju on 21.06.17.
 */

/**
 * Generic event sender interface.
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
