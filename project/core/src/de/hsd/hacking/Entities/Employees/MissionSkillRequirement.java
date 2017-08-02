package de.hsd.hacking.Entities.Employees;

public class MissionSkillRequirement {

    private SkillType skill;
    private float valueRequired;
    private float currentValue;

    public MissionSkillRequirement(SkillType skill, float valueRequired, float currentValue) {
        this.skill = skill;
        this.valueRequired = valueRequired;
        this.currentValue = currentValue;
    }

    public SkillType getSkillType() {
        return skill;
    }

    public float getValueRequired() {
        return valueRequired;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void incrementCurrentValue(float inc) {
        this.currentValue += inc;
    }

    public boolean isSuccessfull(){
        return currentValue >= valueRequired;
    }
}
