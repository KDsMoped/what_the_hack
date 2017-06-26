package de.hsd.hacking.Entities.Employees;

import com.google.gson.annotations.Expose;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public class Skill {

    @Expose private SkillType type;
    @Expose private int value;

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

    public String getDisplayValue(){
//        switch (value){
//            case <3:
//                return "";
//        }
        return String.valueOf(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementSkill() {
        value++;
    }
}
