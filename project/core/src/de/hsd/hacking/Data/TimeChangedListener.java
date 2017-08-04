package de.hsd.hacking.Data;

/**
 * Interface methods for an object that has to be notified about the passage of game time.
 * @author Florian
 */
public interface TimeChangedListener {

    /**
     * Gets called when the internal game time has changed, e.g each frame.
     *
     * @param time The current game time in the range 0-1.0
     */
    void timeChanged(float time);


    /**
     * Gets called when the internal game time has completed one clock time step ( 1/9 day).
     */
    void timeStepChanged(int step);

    /**
     * Gets called when the game day changes and has the new day as parameter.
     * @param days new day in the range 1-365
     */
    void dayChanged(int days);

    /**
     * Gets called when the game week changes and has the new week as parameter.
     * @param week
     */
    void weekChanged(int week);

}
