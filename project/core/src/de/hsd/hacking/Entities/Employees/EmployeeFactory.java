package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.Risky;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.Unreliable;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Utils.MathUtilities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This creates Employees based on the players progress.
 */
public class EmployeeFactory {

    private static final int MAX_SKILL_NUMBER = 4;
    private static final int COST_NEW_SKILL = 14;
    private static final int COST_INCREMENT_SKILL = 6;
    private static final int COST_INCREMENT_ALLPURPOSE = 12;
    private static final float SALARY_VARIANCE = 0.1f;
    private static final int PROGRESS_VARIANCE = 4;

    /**
     * Defines the correlation between game progress and employee competence.
     *
     * @param gameProgress
     * @return
     */
    private static float calcScore(int gameProgress) {
        return 40 + gameProgress * 5;
    }

    /**
     * Defines the Employees salary based on game progress and score.
     *
     * @param gameProgress
     * @param score
     * @return
     */
    private static int calcSalary(int gameProgress, float score) {
        return ((int) ((5 + score * 2) * MathUtilities.mult_var(SALARY_VARIANCE)) * 10);
    }

    /**
     * Creates an Employee based on the players progress.
     *
     * @return
     */
    public static Employee createEmployee() {

        return createEmployee(Team.instance().calcGameProgress());
    }

    /**
     * Creates an Employee based on the given progress.
     *
     * @param gameProgress
     * @return
     */
    public static Employee createEmployee(int gameProgress) {

        int progress = Math.max(0, gameProgress + MathUtilities.var(PROGRESS_VARIANCE));


        Employee freshman = new Employee();
        final float score = calcScore(progress);
        float remainingScore = score;

        freshman.setName(DataLoader.getInstance().getNewName());

        ArrayList<Skill> skillSet = new ArrayList<Skill>();
        skillSet.add(new Skill(SkillType.All_Purpose, 0));

        //sending freshman to university
        remainingScore -= learnSkill(skillSet);

        while (remainingScore > 0) {
            remainingScore -= educateEmployee(freshman, skillSet);
        }

        Collections.sort(skillSet);
        freshman.setSkillSet(skillSet);
        freshman.setSalary(calcSalary(progress, score - remainingScore));

        return freshman;
    }

    /**
     * Creates the given amount of Employees based on the players progress and returns them.
     *
     * @param amount
     * @return
     */
    public static ArrayList<Employee> createEmployees(int amount) {
        return createEmployees(amount, Team.instance().calcGameProgress());
    }

    /**
     * Creates the given amount of Employees based on the given progress and returns them.
     *
     * @param amount
     * @param gameProgress
     * @return
     */
    public static ArrayList<Employee> createEmployees(int amount, int gameProgress) {

        ArrayList<Employee> result = new ArrayList<Employee>();

        for (int i = 0; i < amount; i++) {
            result.add(createEmployee(gameProgress));
        }

        return result;
    }

    /**
     * Rolls for a random feature for the Employee, adds it, and returns its score cost.
     *
     * @param employee
     * @param skillSet
     * @return
     */
    private static float educateEmployee(Employee employee, ArrayList<Skill> skillSet) {

        int roll = MathUtils.random(1, 9);

        switch (roll) {
            case 1:
                return incrementAllpurpose(skillSet);
            case 2:
                return learnSkill(skillSet);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return incrementSkill(skillSet);
            case 9:
                return rollSpecial(employee);
            case 10:
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
     *
     * @param skillSet
     * @return
     */
    private static float learnSkill(ArrayList<Skill> skillSet) {
        if (skillSet.size() >= MAX_SKILL_NUMBER) return 0;

        SkillType skillType;

        do {
            skillType = SkillType.getRandomSkill(true);
        } while (!isUniqueSkill(skillSet, skillType));

        skillSet.add(new Skill(skillType, 1));
        return COST_NEW_SKILL;
    }

    /**
     * Returns true if the SkillType is not already in this list.
     *
     * @param skillSet
     * @param type
     * @return
     */
    private static boolean isUniqueSkill(ArrayList<Skill> skillSet, SkillType type) {
        for (Skill skill : skillSet) {
            if (skill.getType() == type) return false;
        }
        return true;
    }

    /**
     * Increments allpurpose skill and returns its score cost.
     *
     * @param skillSet
     * @return
     */
    private static float incrementAllpurpose(ArrayList<Skill> skillSet) {
        skillSet.get(0).incrementSkill();

        return COST_INCREMENT_ALLPURPOSE;
    }

    /**
     * Increments a skill other than allpurpose and returns its score cost.
     *
     * @param skillSet
     * @return Returns the score cost of this skill.
     */
    private static float incrementSkill(ArrayList<Skill> skillSet) {

        if (skillSet.size() < 2) return 0;

        Skill skill = skillSet.get(MathUtils.random(1, skillSet.size() - 1));

        skill.incrementSkill();

        return COST_INCREMENT_SKILL;
    }

    private static float rollSpecial(Employee padawan){
        int roll = MathUtils.random(1, 3);

        switch (roll) {
            case 1:
                return padawan.addEmployeeSpecial(new Risky(padawan, 2));
            case 2:
                return padawan.addEmployeeSpecial(new Risky(padawan, 1));
            case 3:
                return padawan.addEmployeeSpecial(new Unreliable(padawan, 0.03f));
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
        }

        return 0;
    }
}
