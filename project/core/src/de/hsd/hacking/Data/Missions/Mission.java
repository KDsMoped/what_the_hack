package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.math.MathUtils;
import com.google.gson.annotations.Expose;

import java.util.List;

import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Entities.Employees.Skill;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class represents a mission.
 */
public class Mission implements EventSender {
    @Expose private String name, description;
    @Expose private int duration;
    @Expose private int difficulty;
    @Expose private List<Skill> skill;
    @Expose private MissionOutcome outcome;
    @Expose private float risk;


    /**
     * //Number that gets set when mission is started. To check if mission == mission.
     */
    @Expose private int missionNumber;
    @Expose private boolean finished;
    private boolean completed;

    public Mission(){
        this.missionNumber = -1;
    }

    /**
     * Start the mission.
     */
    public void Start() {
        notifyListeners(EventListener.EventType.MISSION_STARTED);
    }

    /**
     * Abort the mission.
     */
    public void Abort() {
        finished = true;
        notifyListeners(EventListener.EventType.MISSION_ABORTED);
    }

    /**
     * Pause the mission.
     */
    public void Pause() {

    }

    @Override
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(EventListener.EventType type) {

        for (EventListener listener:listeners) {
            listener.OnEvent(type, this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<Skill> getSkill() {
        return skill;
    }

    public void setSkill(List<Skill> skill) {
        this.skill = skill;
    }

    public MissionOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(MissionOutcome outcome) {
        this.outcome = outcome;
    }

    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }


    public void setMissionNumber(int missionNumber1) {
        this.missionNumber = missionNumber1;
    }

    public int getMissionNumber(){
        return this.missionNumber;
    }

    public boolean isFinished() {
        return finished;
    }
    public void setFinished(final boolean finished1) {
        finished = finished1;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed1) {
        this.completed = completed1;
    }
}
