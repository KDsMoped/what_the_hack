package de.hsd.hacking.Data;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cuddl3s on 27.06.2017.
 */

public class GameTime extends Actor {

    private static final float SECONDS_TO_GAME_TIME_DAY_FACTOR = .025f;

    private float currentTime;
    private int currentDay;
    private List<TimeChangedListener> timeChangedListeners;
    private int currentStep;


    public GameTime(int startingDay, float startingTime){
        this.currentDay = startingDay;
        this.currentTime = startingTime;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
    }

    public GameTime(){
        this.currentDay = 0;
        this.currentTime = 0f;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
        this.currentStep = 0;
    }

    @Override
    public void act(float delta) {
        currentTime += delta * SECONDS_TO_GAME_TIME_DAY_FACTOR;
        //Day is over
        if (currentTime >= 1f){
            currentTime = 0f;
            currentDay++;
            for (TimeChangedListener t
                    :
                    timeChangedListeners) {
                t.dayChanged(currentDay);
            }
            if (currentDay % 7 == 0){
                for (TimeChangedListener t:
                     timeChangedListeners) {
                    t.weekChanged((currentDay / 7) + 1);
                }
            }
        }
        //Time [0;1]
        for (TimeChangedListener t :
                timeChangedListeners) {
            t.timeChanged(currentTime);
        }
        //Timesteps [0;8]
        int step = MathUtils.floor(currentTime * 9f);
        if (currentStep < step) {
            currentStep = step;
            for (TimeChangedListener t
                    : timeChangedListeners) {
                t.timeStepChanged(currentStep);
            }
            if (currentStep == 8) currentStep = 0;
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
