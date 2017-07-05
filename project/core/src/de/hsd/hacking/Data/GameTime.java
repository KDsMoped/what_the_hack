package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 27.06.2017.
 */

public class GameTime extends Actor {

    public static final GameTime instance = new GameTime();

    private static final float SECONDS_TO_GAME_TIME_DAY_FACTOR = .05f;
    private static final int CLOCK_STEPS = 9;

    private float currentTime;
    private int currentDay;
    private List<TimeChangedListener> timeChangedListeners;
    private int currentStep;


    //for de-serializing?
    /*public GameTime(int startingDay, float startingTime){
        this.currentDay = startingDay;
        this.currentTime = startingTime;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
    }*/

    private GameTime(){
        this.currentDay = 1;
        this.currentTime = 0f;
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
        this.currentStep = 0;
    }

    @Override
    public void act(float delta) {
        currentTime += delta * SECONDS_TO_GAME_TIME_DAY_FACTOR;

        //Day is over
        if (currentTime >= 1f) {
            currentTime = 0f;
            currentDay++;
            Gdx.app.log(Constants.TAG, "Day changed. Now day: " + currentDay);
            for (TimeChangedListener t:timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
                t.dayChanged(currentDay);
            }
            if (currentDay % 7 == 0) {
                for (TimeChangedListener t: timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {t.weekChanged((currentDay / 7) + 1);
                }
            }
            currentStep = 0;
            for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {t.timeStepChanged(currentStep);
            }
        }
        //Time [0;1]
        for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
            t.timeChanged(currentTime);
        }
        //Timesteps [0;8]
        int step = MathUtils.floor(currentTime * (CLOCK_STEPS));
        if (currentStep < step) {
            currentStep = step;
            for (TimeChangedListener t
                    : timeChangedListeners) {
                t.timeStepChanged(currentStep);
            }
        }
    }

    public void addTimeChangedListener(final TimeChangedListener listener) {
        if (!timeChangedListeners.contains(listener)) {
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

    public boolean removeTimeChangedListener(TimeChangedListener timeChangedListener) {
        return timeChangedListeners.remove(timeChangedListener);
    }
}
