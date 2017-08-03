package de.hsd.hacking.Data.Missions;

import de.hsd.hacking.Proto;

/**
 * This class is only used to deserialize the skills.
 * @author Julian Geywitz
 */
public class SkillHolder {
    private int value;
    private Proto.SkillType type;

    public SkillHolder() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Proto.SkillType getType() {
        return type;
    }

    public void setType(Proto.SkillType type) {
        this.type = type;
    }
}
