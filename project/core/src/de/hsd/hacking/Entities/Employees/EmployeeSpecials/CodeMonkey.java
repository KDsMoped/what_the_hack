package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.RandomUtils;

/**
 * This employee special improves employees software skills while rducing social dice throws.
 *
 * @author Hendrik
 */
public class CodeMonkey extends EmployeeSpecial {

    public CodeMonkey(Employee employee) {
        super(employee);
    }

    @Override
    public String getDisplayName() {
        return "Code Monkey";
    }

    @Override
    public String getDescription() {
        return "Programming expertise at minimum hygiene.";
    }

    @Override
    public float getScoreCost() {
        return 8;
    }

    @Override
    public boolean isApplicable() {
        return employee.hasSkill(Proto.Skill.SkillType.Software);
    }

    @Override
    public float getSkillRelativeFactor(SkillType type) {

        if(type.skillType == Proto.Skill.SkillType.Software){
            return 1.4f;
        }

        if(type.skillType == Proto.Skill.SkillType.Social){
            return 0.6f;
        }

        return 1;
    }

    @Override
    public int getSkillAbsoluteBonus(SkillType type) {
        return 1;
    }
}
