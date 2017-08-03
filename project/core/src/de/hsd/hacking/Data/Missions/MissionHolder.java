package de.hsd.hacking.Data.Missions;

import java.util.List;


/**
 * This class is only used to deserialize the mission objects.
 * @author Julian
 */
public class MissionHolder {
    private String name, description, onSuccess, onFail;
    private int duration;
    private int difficulty;
    private float hardness = 1;
    private List<SkillHolder> skill;
    private float risk;
    private int minLevel;
    private int maxLevel;
    private int usedBandwidth;

    public MissionHolder() {

    }

    public int getUsedBandwidth() {
        return usedBandwidth;
    }

    public void setUsedBandwidth(int usedBandwidth) {
        this.usedBandwidth = usedBandwidth;
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

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    public String getOnFail() {
        return onFail;
    }

    public void setOnFail(String onFail) {
        this.onFail = onFail;
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

    public float getHardness() {
        return hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public List<SkillHolder> getSkill() {
        return skill;
    }

    public void setSkill(List<SkillHolder> skill) {
        this.skill = skill;
    }

    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * Clones the data of this mission into an actual usable Mission object.
     * @return Mission object to use in the game.
     */
    public Mission Clone() {
        Mission mission = new Mission(this);
        return mission;
    }
}
