package de.hsd.hacking.Utils.Callback;

import de.hsd.hacking.Data.Missions.Mission;

/**
 * Callback interface for various callbacks that send a {@link Mission}.
 *
 * @author Hendrik
 */
public interface MissionCallback {
    void callback(Mission mission);
}
