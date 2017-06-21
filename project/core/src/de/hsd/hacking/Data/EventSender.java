package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ju on 21.06.17.
 */

public interface EventSender {
    List<EventListener> listeners = new ArrayList<EventListener>();

    public void addListener(EventListener listener);
    public void removeListener(EventListener listener);
    public void notifyListeners(EventListener.EventType type);
}
