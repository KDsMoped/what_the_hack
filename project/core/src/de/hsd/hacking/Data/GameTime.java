package de.hsd.hacking.Data;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.UI.StatusBar;

/**
 * Created by Cuddl3s on 27.06.2017.
 */

public class GameTime extends Actor {

    private static final float SECONDS_TO_GAME_TIME_FACTOR = .025f;

    private float currentTime;
    private int currentDay;
    private List<TimeChangedListener> timeChangedListeners;


    public GameTime(int startingDay, float startingTime){
        this.currentDay = startingDay;
        this.currentTime = startingTime;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
    }

    public GameTime(){
        this.currentDay = 0;
        this.currentTime = 0f;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
    }

    @Override
    public void act(float delta) {
        currentTime += delta * SECONDS_TO_GAME_TIME_FACTOR;
        if (currentTime >= 1f){
            currentTime = 0f;
            currentDay++;
            for (TimeChangedListener t :
                    timeChangedListeners) {
                t.dayChanged(currentDay);
            }
        }
        for (TimeChangedListener t :
                timeChangedListeners) {
            t.timeChanged(currentTime);
        }
    }

    public void addTimeChangedListener(TimeChangedListener listener) {
        if (!timeChangedListeners.contains(listener)){
            timeChangedListeners.add(listener);
        }
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public List<TimeChangedListener> getTimeChangedListeners() {
        return timeChangedListeners;
    }

    public void setTimeChangedListeners(List<TimeChangedListener> timeChangedListeners) {
        this.timeChangedListeners = timeChangedListeners;
    }
}
