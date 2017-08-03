package de.hsd.hacking.Utils.Provider;

import de.hsd.hacking.Data.Missions.Mission;

/**
 * Used for references to a {@link Mission} that are evaluated when called. This should usually be implemented by an anonymous inner class.
 *
 * @author Hendrik
 */
public interface MissionProvider {
    Mission get();
}
