package de.hsd.hacking.Data;

/**
 * Created by ju on 21.06.17.
 */

public interface EventListener {
    public enum EventType {
        MISSION_STARTED, MISSION_FINISHED, MISSION_ABORTED
    }

    public void OnEvent(EventType type);
}