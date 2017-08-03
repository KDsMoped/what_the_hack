package de.hsd.hacking.Entities.Employees;

import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Proto;

/**
 * This class represents a skill requirement and the actual working status of a mission.
 * @author Florian, Julian
 */
public class MissionSkillRequirement implements DataContainer {

    Proto.MissionSkillRequirement.Builder data;
    private SkillType skill;

    public MissionSkillRequirement(SkillType skill, float valueRequired, float currentValue) {
        data = Proto.MissionSkillRequirement.newBuilder();
        this.skill = skill;
        data.setRequired(valueRequired);
        data.setCurrent(currentValue);
    }

    public MissionSkillRequirement(Proto.MissionSkillRequirement.Builder builder) {
        this.data = builder;
        this.skill = new SkillType(data.getSkillType());
    }

    public SkillType getSkillType() {
        return skill;
    }

    public float getValueRequired() {
        return data.getRequired();
    }

    public float getCurrentValue() {
        return data.getCurrent();
    }

    public void incrementCurrentValue(float inc) {
        data.setCurrent(data.getCurrent() + inc);
    }

    public boolean isSuccessfull(){
        return data.getCurrent() >= data.getRequired();
    }

    public Proto.MissionSkillRequirement getData() {
        data.setSkillType(skill.skillType);

        return data.build();
    }
}
