package de.hsd.hacking.Data;

/**
 * Created by ju on 21.06.17.
 */

/**
 * A generic event listener interface.
 */
public interface EventListener {
    /**
     * These are the different event types we use.
     */
    enum EventType {
        MISSION_STARTED, MISSION_FINISHED, MISSION_ABORTED, MESSAGE_NEW, POPUP_SHOWN, POPUP_CLOSED
    }

    /**
     * Gets called when the event is fired.
     * @param type Type of the fired event.
     * @param sender Object that fired the event.
     */
    void OnEvent(EventType type, Object sender);
}