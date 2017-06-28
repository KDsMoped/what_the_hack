package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Team.Team;
import java.util.ArrayList;

/**
 * This creates Employees based on the players progress.
 */
public class EmployeeFactory {

    private static final int MAX_SKILL_NUMBER = 4;
    private static final int COST_NEW_SKILL = 14;
    private static final int COST_INCREMENT_SKILL = 4;
    private static final int COST_INCREMENT_ALLPURPOSE = 14;
    private static final float SALARY_VARIANCE = 0.1f;

    /**
     * Defines the correlation between game progress and employee competence.
     * @param gameProgress
     * @return
     */
    private static float calcScore(int gameProgress) {
        return 50 + gameProgress * 5;
    }

    /**
     * Defines the Employees salary based on game progress and score.
     * @param gameProgress
     * @param score
     * @return
     */
    private static int calcSalary(int gameProgress, float score) {
        return (int) ((10 + score * 5) * MathUtils.random(1 - SALARY_VARIANCE, 1 + SALARY_VARIANCE) * 10);
    }

    /**
     * Creates an Employee based on the players progress.
     *
     * @return
     */
    public static Employee CreateEmployee() {

        return CreateEmployee(Team.instance().calcGameProgress());
    }

    /**
     * Creates an Employee based on the given progress.
     *
     * @param gameProgress
     * @return
     */
    public static Employee CreateEmployee(int gameProgress) {

        Employee freshman = new Employee();
        final float score = calcScore(gameProgress);
        float remainingScore = score;

        freshman.setName(DataLoader.getInstance().getNewName());

        ArrayList<Skill> skillSet = new ArrayList<Skill>();
        skillSet.add(new Skill(SkillType.All_Purpose, 0));

        //sending freshman to university
        remainingScore -= LearnSkill(skillSet);

        while (remainingScore > 0) {
            remainingScore -= EducateEmployee(freshman, skillSet);
        }

        freshman.setSkillSet(skillSet);
        freshman.setSalary(calcSalary(gameProgress, score - remainingScore));

        return freshman;
    }

    /**
     * Creates the given amount of Employees based on the players progress and returns them.
     * @param amount
     * @return
     */
    public static ArrayList<Employee> CreateEmployees(int amount) {
        return CreateEmployees(amount, Team.instance().calcGameProgress());
    }

    /**
     * Creates the given amount of Employees based on the given progress and returns them.
     * @param amount
     * @param gameProgress
     * @return
     */
    public static ArrayList<Employee> CreateEmployees(int amount, int gameProgress) {

        ArrayList<Employee> result = new ArrayList<Employee>();

        for (int i = 0; i < amount; i++) {
            result.add(CreateEmployee(gameProgress));
        }

        return result;
    }

    /**
     * Rolls for a random feature for the Employee, adds it, and returns its score cost.
     * @param employee
     * @param skillSet
     * @return
     */
    private static float EducateEmployee(Employee employee, ArrayList<Skill> skillSet) {

        int roll = MathUtils.random(1, 8);

        switch (roll) {
            case 1:
                return IncrementAllpurpose(skillSet);
            case 2:
                return LearnSkill(skillSet);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return IncrementSkill(skillSet);
            case 11:
            case 12:
            case 13:

                //TODO: Implement more features for Employees!

            case 20:
                //this is a magic employee and gets a super skill!
        }

        return 0;
    }

    /**
     * Learns a random skill and returns its cost.
     * @param skillSet
     * @return
     */
    private static float LearnSkill(ArrayList<Skill> skillSet) {
        if (skillSet.size() >= MAX_SKILL_NUMBER) return 0;

        SkillType skillType;

        do {
            skillType = SkillType.getRandomSkill();
        } while (!IsUniqueSkill(skillSet, skillType));

        skillSet.add(new Skill(skillType, 1));
        return COST_NEW_SKILL;
    }

    /**
     * Returns true if the SkillType is not already in this list.
     * @param skillSet
     * @param type
     * @return
     */
    private static boolean IsUniqueSkill(ArrayList<Skill> skillSet, SkillType type) {
        for (Skill skill : skillSet) {
            if (skill.getType() == type) return false;
        }
        return true;
    }

    /**
     * Increments allpurpose skill and returns its score cost.
     * @param skillSet
     * @return
     */
    private static float IncrementAllpurpose(ArrayList<Skill> skillSet) {
        skillSet.get(0).incrementSkill();

        return COST_INCREMENT_ALLPURPOSE;
    }

    /**
     * Increments a skill other than allpurpose and returns its score cost.
     *
     * @param skillSet
     * @return Returns the score cost of this skill.
     */
    private static float IncrementSkill(ArrayList<Skill> skillSet) {

        if (skillSet.size() < 2) return 0;

        Skill skill = skillSet.get(MathUtils.random(1, skillSet.size() - 1));

        skill.incrementSkill();

        return COST_INCREMENT_SKILL;
    }
}
