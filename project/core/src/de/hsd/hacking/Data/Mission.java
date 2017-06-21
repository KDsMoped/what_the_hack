package de.hsd.hacking.Data;

import com.google.gson.annotations.Expose;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class represents a mission.
 */
public class Mission implements EventSender {
    @Expose private String name, description;
    private int duration;
    private int difficulty;
    private List<Skill> skill;
    private MissionOutcome outcome;

    public void Start() {
        notifyListeners(EventListener.EventType.MISSION_STARTED);
    }

    public void Abort() {
        notifyListeners(EventListener.EventType.MISSION_ABORTED);
    }

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
            listener.OnEvent(type);
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
}
