package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.*;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This creates Employees based on the players progress. It creates their skill set and cares for automatic level up.
 *
 * @author Hendrik
 */
public class EmployeeFactory {

    private static final int MAX_SKILL_NUMBER = 4;
    private static final int COST_NEW_SKILL = 14;
    private static final int COST_INCREMENT_SKILL = 6;
    private static final int COST_INCREMENT_ALLPURPOSE = 12;
    private static final float SALARY_VARIANCE = 0.1f;
    private static final int PROGRESS_VARIANCE = 6;
    private static final int LEVELUP_THRESHOLD = 10;

    public static final float SCORE_MISSION_COMPLETED = 2;
    public static final float SCORE_MISSION_COMPLETED_PERLEVEL = 0.5f;
    public static final float SCORE_MISSION_CRITICAL_SUCCESS = 0.1f;



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
        return ((int) ((5 + score * 2) * RandomUtils.mult_var(SALARY_VARIANCE)) * 10);
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

        int progress = Math.max(0, gameProgress + RandomUtils.var(PROGRESS_VARIANCE));


        Employee freshman = new Employee();
//        final float score = calcScore(progress);
        freshman.incrementFreeScore(calcScore(progress));

//        float remainingScore = score;

        Proto.Employee.Gender gender = Employee.randomGender();
        freshman.setGender(gender);
        freshman.setName(DataLoader.getInstance().getNewName(gender));

        //spend score points on random attributes and skills
        learnBasicSkillSet(freshman);

//        freshman.incrementUsedScore(score - remainingScore);
        freshman.setSalary(calcSalary(progress, freshman.getUsedScore()/* + freshman.getFreeScore()*/));

        return freshman;
    }

    /**
     * Creates an employees basic skillSet and levels it according to the available score points. Returns the remaining score points.
     *
     * @param employee
     * @return
     */
    private static void learnBasicSkillSet(Employee employee){

        ArrayList<Skill> skillSet = new ArrayList<Skill>();
        skillSet.add(new Skill(Proto.Skill.SkillType.All_Purpose, 1));
        employee.setSkillSet(skillSet);

        //sending freshman to university
        employee.useScore(learnSkill(employee, skillSet));

        spendScorePoints(employee, skillSet);
    }

    /**
     * Spends all free score points of this employee.
     * @param employee
     */
    public static void spendScorePoints(Employee employee) {

        spendScorePoints(employee, employee.getSkillset());
    }

    /**
     * Spends all free score points of this employee.
     * @param employee
     */
    private static void spendScorePoints(Employee employee, Collection<Skill> skillSet) {

        while (employee.getFreeScore() > 0) {
            employee.useScore(educateEmployee(employee, skillSet));
        }

        employee.sortSkills();
    }

    /**
         * Rolls for a random feature for the Employee, adds it, and returns its score cost.
         *
         * @param employee
         * @param skillSet
         * @return
         */
    private static float educateEmployee(Employee employee, Collection<Skill> skillSet) {

        int roll = RandomUtils.randomIntWithin(1, 12);

        switch (roll) {
            case 1:
                return incrementAllpurpose(skillSet);
            case 2:
                return learnSkill(employee, skillSet);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return incrementSkill(skillSet);
            case 10:
            case 11:
                return rollSpecial(employee);
            case 12:
                return 1; //wasting time playing video games
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
    private static float learnSkill(Employee employee, Collection<Skill> skillSet) {

        if (skillSet.size() >= MAX_SKILL_NUMBER) return 0;

        Proto.Skill.SkillType skillType;

        do {
            skillType = SkillType.getRandomSkill(true);
        } while (!isUniqueSkill(skillSet, skillType));

        employee.learnSkill(new Skill(skillType, 1), false);

        return COST_NEW_SKILL;
    }

    /**
     * Returns true if the SkillType is not already in this list.
     *
     * @param skillSet
     * @param type
     * @return
     */
    private static boolean isUniqueSkill(Collection<Skill> skillSet, Proto.Skill.SkillType type) {
        for (Skill skill : skillSet) {
            if (skill.getType().skillType == type) return false;
        }
        return true;
    }

    /**
     * Increments allpurpose skill and returns its score cost.
     *
     * @param skillSet
     * @return
     */
    private static float incrementAllpurpose(Collection<Skill> skillSet) {

        for (Skill skill :skillSet) {
            if (skill.getType().skillType  != Proto.Skill.SkillType.All_Purpose) continue;

            skill.incrementSkill();
            return COST_INCREMENT_ALLPURPOSE;
        }

        Gdx.app.error(Constants.TAG, "Error: Employee does not have all-purpose skill!");
        return 0;
    }

    /**
     * Increments a skill other than allpurpose and returns its score cost.
     *
     * @param skillSet
     * @return Returns the score cost of this skill.
     */
    private static float incrementSkill(Collection<Skill> skillSet) {

        if (skillSet.size() < 2) return 0;

        Skill skill;
        do {
            skill = skillSet.toArray(new Skill[skillSet.size()])[RandomUtils.randomIntWithin(0, skillSet.size() - 1)];
        }while (skill.getType().skillType == Proto.Skill.SkillType.All_Purpose);

        skill.incrementSkill();

        return COST_INCREMENT_SKILL;
    }

    /**
     * Adds a special ability to this employee and returns its score.
     * @param padawan
     * @return
     */
    private static float rollSpecial(Employee padawan){
        int roll = RandomUtils.randomIntWithin(1, 8);

        switch (roll) {
            case 1:
                return padawan.addEmployeeSpecial(new Risky(padawan, 2));
            case 2:
                return padawan.addEmployeeSpecial(new Risky(padawan, 1));
            case 3:
                return padawan.addEmployeeSpecial(new Unreliable(padawan));
            case 4:
                return padawan.addEmployeeSpecial(new CheapToHire(padawan));
            case 5:
                return padawan.addEmployeeSpecial(new FastLearner(padawan));
            case 6:
            case 7:
                return padawan.addEmployeeSpecial(new CodeMonkey(padawan));
            case 8:
                return padawan.addEmployeeSpecial(new LuckyDevil(padawan));
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
        }

        return 0;
    }

    /**
     * Given that the employees free score point exceed the level-up threshold,
     * @param employee
     */
    public static void levelUp(Employee employee){
        if(employee.getFreeScore() < LEVELUP_THRESHOLD) return;

        Gdx.app.log(Constants.TAG, employee.getName() + " levels up!");

        spendScorePoints(employee);

        Gdx.app.log(Constants.TAG, employee.getName() + " has now " + employee.getUsedScore() + " score.");

        employee.onLevelUp();
    }
}
