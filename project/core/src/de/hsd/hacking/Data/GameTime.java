package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.protobuf.Internal;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Proto.Global;
import de.hsd.hacking.Utils.Constants;

/**
 * This class manages the game time.
 * @author Florian Julian
 */
public class GameTime extends Actor implements Manager, DataContainer {
    // all persistent data is in here
    Global.Builder data;
    private static GameTime instance;

    /**
     * Creates an instance of this manager.
     */
    public static void createInstance() {
        if (instance != null){
            Gdx.app.error("", "ERROR: Instance of GameTime has not been destroyed before creating new one.");
            return;
        }

        instance = new GameTime();
    }

    /**
     * Provides an instance of this manager;
     * @return
     */
    public static GameTime instance() {

        if (instance == null)
            Gdx.app.error("", "ERROR: Instance of GameTime has not been created yet. Use createInstance() to do so.");

        return instance;
    }

    private static final float SECONDS_TO_GAME_TIME_DAY_FACTOR = .025f;
    private static final int CLOCK_STEPS = 9;

    private List<TimeChangedListener> timeChangedListeners;

    private GameTime() {
        data = Global.newBuilder();

        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
    }

//    public GameTime(Global.Builder data) {
//        this.data = data;
//
//        timeChangedListeners = new ArrayList<TimeChangedListener>(4);
//        instance = this;
//    }

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

    public Global getData() {
        return data.build();
    }

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    @Override
    public void initReferences() {
    }

    /**
     * Creates the default state of this manager when a new game is started.
     */
    @Override
    public void loadDefaultState() {
        data.setDay(1);
        data.setTime(0f);
        data.setCurrentStep(0);
    }

    /**
     * Recreates the state this manager had before serialization.
     */
    @Override
    public void loadState() {
        data = SaveGameManager.getGameTime();
    }

    /**
     * Destroys this manager instance.
     */
    @Override
    public void cleanUp() {
        instance = null;
    }
}
