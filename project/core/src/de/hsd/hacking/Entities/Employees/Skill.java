package de.hsd.hacking.Entities.Employees;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public class Skill {

    private SkillType type;
    private int value;

    public Skill(SkillType type, int value){
        this.type = type;
        this.value = value;
    }

    public SkillType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementSkill() {
        value++;
    }
}
