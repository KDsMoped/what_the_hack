package de.hsd.hacking.Data;

import java.util.AbstractMap;
import java.util.List;

import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class represents a mission.
 */
public class Mission {
    private String name, description;
    private int duration;
    private int difficulty;
    private List<Skill> skill;
    private MissionOutcome outcome;

    public void Start() {

    }

    public void Abort() {

    }

    public void Pause() {

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
