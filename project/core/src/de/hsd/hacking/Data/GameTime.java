package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.protobuf.Internal;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Proto.Global;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 27.06.2017.
 */

public class GameTime extends Actor {
    Global.Builder data;
    public static GameTime instance;

    private static final float SECONDS_TO_GAME_TIME_DAY_FACTOR = .025f;
    private static final int CLOCK_STEPS = 9;

    private List<TimeChangedListener> timeChangedListeners;

    public GameTime() {
        data = Global.newBuilder();

        data.setDay(1);
        data.setTime(0f);
        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
        data.setCurrentStep(0);

        instance = this;
    }

    public GameTime(Global.Builder data) {
        this.data = data;

        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
        instance = this;
    }

    @Override
    public void act(float delta) {
        data.setTime(data.getTime() + delta * SECONDS_TO_GAME_TIME_DAY_FACTOR);

        //Day is over
        if (data.getTime() >= 1f) {
            data.setTime(0f);
            data.setDay(data.getDay() + 1);
            if(Constants.DEBUG) Gdx.app.log(Constants.TAG, "Day changed. Now day: " + data.getDay());
            for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
                t.dayChanged(data.getDay());
            }
            if (data.getDay() % 7 == 0) {
                for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
                    t.weekChanged((data.getDay() / 7) + 1);
                }
            }
            data.setCurrentStep(0);
            for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
                t.timeStepChanged(data.getCurrentStep());
            }
        }
        //Time [0;1]
        for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
            t.timeChanged(data.getTime());
        }
        //Timesteps [0;8]
        int step = MathUtils.floor(data.getTime() * (CLOCK_STEPS));
        if (data.getCurrentStep() < step) {
            data.setCurrentStep(step);
            for (TimeChangedListener t : timeChangedListeners.toArray(new TimeChangedListener[timeChangedListeners.size()])) {
                t.timeStepChanged(data.getCurrentStep());
            }
        }
    }

    public void addTimeChangedListener(final TimeChangedListener listener) {
        if (!timeChangedListeners.contains(listener)) {
            timeChangedListeners.add(listener);
        }
    }

    public float getCurrentTime() {
        return data.getTime();
    }

    public void setCurrentTime(float currentTime) {
        data.setTime(currentTime);
    }

    public int getCurrentDay() {
        return data.getDay();
    }

    public void setCurrentDay(int currentDay) {
        data.setDay(currentDay);
    }

    public int getCurrentStep() {
        return data.getCurrentStep();
    }

    public void setCurrentStep(int currentStep) {
        data.setCurrentStep(currentStep);
    }

    public List<TimeChangedListener> getTimeChangedListeners() {
        return timeChangedListeners;
    }

    public void setTimeChangedListeners(List<TimeChangedListener> timeChangedListeners) {
        this.timeChangedListeners = timeChangedListeners;
    }

    public float getRemainingWeekFraction(){
        return 1 - ((data.getDay()) % 7) / (float) 7;
    }

    public boolean removeTimeChangedListener(TimeChangedListener timeChangedListener) {
        return timeChangedListeners.remove(timeChangedListener);
    }

    public Global.Builder getData() {
        return data;
    }
}
