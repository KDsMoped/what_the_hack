package de.hsd.hacking.Entities.Employees;

import com.google.gson.annotations.Expose;

import java.util.Comparator;

import de.hsd.hacking.Data.Missions.SkillHolder;
import de.hsd.hacking.Proto;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public class Skill implements Comparable<Skill>{
    private Proto.Skill.Builder data;

    public Skill() {
        this.data = Proto.Skill.newBuilder();
    }

    public Skill(SkillHolder holder) {
        this.data = Proto.Skill.newBuilder();

        data.setValue(holder.getValue());
        data.setType(holder.getType());
    }

    public Skill(Proto.Skill.Builder data) {
        this.data = data;
    }

    public Skill(Proto.Skill.SkillType type, int value){
        this.data = Proto.Skill.newBuilder();
        this.data.setType(type);
        this.data.setValue(value);
    }

    public SkillType getType() {
        return new SkillType(data.getType());
    }

    public int getValue() {
        return data.getValue();
    }

    public String getDisplayType(){
        return new SkillType(data.getType()).getDisplayName();
    }

    public String getDisplayText(){

        if(data.getValue() < 4 ) return "Newbie";
        if(data.getValue() <  6) return "Greenhorn";
        if(data.getValue() <  8) return "Beginner";
        if(data.getValue() < 10) return "Intermediate";
        if(data.getValue() < 12) return "Experienced";
        if(data.getValue() < 14) return "Professional";
        if(data.getValue() < 16) return "Master";
        if(data.getValue() < 18) return "Grand Master";
        if(data.getValue() < 20) return "Wizard";
        return "God";
    }

    public String getDisplayValue(boolean includeText){

        if(includeText) return getDisplayText() + " " + getDisplayValue(false) + "";

        if(data.getValue() < 10) return " " + String.valueOf(data.getValue());
        else return String.valueOf(data.getValue());
    }

    public void setValue(int value) {
        data.setValue(value);
    }

    public void incrementSkill() {
        data.setValue(data.getValue() + 1);
    }

    @Override
    public int compareTo(Skill o) {
        return ((Integer) o.getValue()).compareTo(data.getValue());
    }
}
