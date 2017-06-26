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

    public String getDisplayText(){

        if(value < 4 ) return "Newbie";
        if(value <  6) return "Greenhorn";
        if(value <  8) return "Beginner";
        if(value < 10) return "Intermediate";
        if(value < 12) return "Experienced";
        if(value < 14) return "Professional";
        if(value < 16) return "Master";
        if(value < 18) return "Grand Master";
        if(value < 20) return "Wizard";
        return "God";
    }

    public String getDisplayValue(boolean includeText){

        if(includeText) return getDisplayText() + " " + getDisplayValue(false) + "";

        return String.valueOf(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementSkill() {
        value++;
    }
}
