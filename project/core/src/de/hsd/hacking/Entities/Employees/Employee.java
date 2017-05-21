package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Utils.FromTo;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class Employee {


    public enum EmployeeSkillLevel {
        NOOB, INTERMEDIATE, PRO, WIZARD;

        private static final EmployeeSkillLevel[] VALUES = values();
        public static final int SIZE = VALUES.length;

        public static EmployeeSkillLevel getRandomSkillLevel() { return VALUES[MathUtils.random(SIZE - 1)]; }
    }

    //Data
    private String surName;
    private String lastName;
    private String description; // ? Needed ?
    private EmployeeSkillLevel skillLevel;

    private ArrayList<Skill> skillSet;

    /**
     * Creates a new random employee
     * @param level The desired skill Level
     */
    public Employee(EmployeeSkillLevel level){

        //Create random name
        String[] randomName = DataLoader.getInstance().getNewName();
        this.surName = randomName[0];
        this.lastName = randomName[1];
        this.skillLevel = level;

        //Skill points to spend. NOOB = 55, INTERMEDIATE = 65, PRO = 75, WIZARD = 85
        //35 Points are spend by default (5 per Skill)
        int skillPoints = 55 + skillLevel.ordinal() * 10;
        skillSet = new ArrayList<Skill>(7);
        for (SkillType type :
                SkillType.values()) {
            skillSet.add(new Skill(type, 5));
            skillPoints -= 5;
        }

        RandomIntPool pool = new RandomIntPool(new FromTo(0, skillSet.size() - 1));

        while (skillPoints > 0){
            int randomInt = pool.getRandomNumberWithoutRemoving();
            Skill incrementSkill = skillSet.get(randomInt);
            if (incrementSkill.getValue() < 15){
                incrementSkill.incrementSkill();
                skillPoints--;
            }else{ //Skill at max -> Remove skill index from possible random indexes
                pool.removeNumber(randomInt);
            }
        }
    }

    @Override
    public String toString(){
        return "Employee: " + surName + " " + lastName + ". Skilllevel: " + this.skillLevel.name() + ". Skills: " + skillSetToString();
    }

    private String skillSetToString(){
        String skills = "";
        for (Skill skill :
                skillSet) {
            skills += skill.getType().name() + " : " + skill.getValue() + "; ";
        }
        return skills;
    }


}
