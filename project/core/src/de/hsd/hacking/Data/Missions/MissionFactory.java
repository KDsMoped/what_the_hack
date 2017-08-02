package de.hsd.hacking.Data.Missions;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.RandomUtils;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class contains methods to generate various mission objects.
 */
public final class MissionFactory {

    private static final float MISSION_DIFFICULTY_VARIANCE = 0.15f;
    private static final int MISSION_DURATION_MINIMUM = 2;
    private static final int MISSION_DURATION_VARIANCE = 1;
    private static final float MISSION_REWARDMONEY_VARIANCE = 0.15f;
    private static final int MISSION_REWARDMONEY_FACTOR = 16;
    private static final int MISSION_BASE_POWER = 9;
    private static final int MISSION_BANDWIDTH_BASE = 100;
    private static final float MISSION_BANDWIDTH_PER_DIFFICULTY = 0.1f;
    private static final float SKILL_POWER_PER_DIFFICULTY = 2.5f;
    private static final float SKILL_DIFFICULTY_VARIANCE = 0.4f;

    /**
     * Create a new random mission object with the required skill values based on the given difficulty.
     *
     * @param difficulty
     * @return Random mission with defined difficulty.
     */
    public static Mission CreateRandomMission(int difficulty) {
        Mission mission = DataLoader.getInstance().getNewMission(difficulty);

        ReplacePlaceholders(mission);
        calcDurationVariance(mission);
        RandomSkillValues(mission, difficulty);
        calcBandwidth(mission);

        generateOutcome(mission);

        return mission;
    }

    /**
     * Varies the duration of this mission.
     *
     * @param mission
     */
    private static void calcDurationVariance(Mission mission) {
        mission.setDuration(Math.max(MISSION_DURATION_MINIMUM, mission.getDuration() + RandomUtils.var(MISSION_DURATION_VARIANCE)));
    }

    /**
     * Generates an outcome with difficulty based rewardMoney
     *
     * @param mission
     */
    private static void generateOutcome(Mission mission) {
        mission.setRewardMoney(MissionFactory.calcRewardMoney(mission));
    }

    /**
     * Gives the skills required for this mission random values based on the given difficulty.
     *
     * @param mission The mission that needs new skill values.
     */
    private static void RandomSkillValues(Mission mission, int baseDifficulty) {

        List<Skill> skills = mission.getSkill();

        int difficulty = calcMissionLevel(baseDifficulty + MISSION_BASE_POWER, mission.getHardness(), mission.getDuration());
        int numSkills = skills.size();
        mission.setDifficulty(difficulty);

        float difficultyPerSkill = calcSkillDifficulty(difficulty, numSkills);
        for (Skill s : skills) {
            s.setValue((int) (difficultyPerSkill * RandomUtils.mult_var(SKILL_DIFFICULTY_VARIANCE)));
        }
    }

    /**
     * Calculates the difficulty of a single skill in a mission based on number of skills in that mission.
     *
     * @param difficulty
     * @param numSkills
     * @return
     */
    private static float calcSkillDifficulty(int difficulty, int numSkills) {
        return Math.min(1, (difficulty * 2) / (float) (numSkills + 0.9)) * SKILL_POWER_PER_DIFFICULTY;
    }

    /**
     * Calculates the difficulty of the mission based on a baseDifficulty.
     *
     * @param baseDifficulty
     * @param hardness
     * @param duration
     * @return
     */
    private static int calcMissionLevel(int baseDifficulty, float hardness, int duration) {
        return Math.max(1, (int) (
                baseDifficulty
                        * hardness
//                        * durationDifficultyFactor(duration)
                        * RandomUtils.mult_var(MISSION_DIFFICULTY_VARIANCE)));
    }

    /**
     * Calculates the bandwidth used by this mission based on its difficulty.
     * @param mission
     */
    public static void calcBandwidth(Mission mission) {
        mission.setUsedBandwidth(((int) (mission.getDifficulty() * MISSION_BANDWIDTH_PER_DIFFICULTY)) * 100 + MISSION_BANDWIDTH_BASE);
    }

    /**
     * Calculates a reward for the mission
     *
     * @param mission
     * @return
     */
    public static int calcRewardMoney(Mission mission) {
        return (int) (mission.getDifficulty() * (1 + mission.getRisk()) * RandomUtils.mult_var(MISSION_REWARDMONEY_VARIANCE) * MISSION_REWARDMONEY_FACTOR) * 10;
    }

    /**
     * Defines the relation between the duration and difficulty of a mission.
     *
     * @param duration
     * @return
     */
    private static float durationDifficultyFactor(int duration) {

        return Math.min(2, 1f + 2f / duration);
    }

    /**
     * Replaces the %XXX% placeholders in mission data with random data.
     *
     * @param mission
     */
    private static void ReplacePlaceholders(Mission mission) {

        DataLoader dl = DataLoader.getInstance();

        ReplacePlaceholder(mission, "%CONTACT%", dl.getNewFullName(Proto.Employee.Gender.UNDECIDED));
        ReplacePlaceholder(mission, "%CONTACT_L%", dl.getNewLastName());
        ReplacePlaceholder(mission, "%CONTACT_M%", dl.getNewFullName(Proto.Employee.Gender.MALE));
        ReplacePlaceholder(mission, "%CONTACT_F%", dl.getNewFullName(Proto.Employee.Gender.FEMALE));
        ReplacePlaceholder(mission, "%COMPANY%", dl.getNewCompanyName());
        ReplacePlaceholder(mission, "%PW_APPLICATION%", dl.getNewPasswordApplication());
        ReplacePlaceholder(mission, "%UNIVERSITY%", dl.getNewUniversityName());
        ReplacePlaceholder(mission, "%WEBSERVICE%", dl.getNewWebServiceName());
        ReplacePlaceholder(mission, "%SOFTWARE%", dl.getNewSoftwareName());
        ReplacePlaceholder(mission, "%TOWN%", dl.getNewTown());
        ReplacePlaceholder(mission, "%COUNTRY%", dl.getNewCountryName());
        ReplacePlaceholder(mission, "%INSTITUTION%", dl.getNewInstitution());
    }

    private static void ReplacePlaceholder(Mission mission, String placeholder, String token) {
        mission.setName(mission.getName().replaceAll(placeholder, token));
        mission.setDescription(mission.getDescription().replaceAll(placeholder, token));
        mission.setSuccessText(mission.getSuccessText().replaceAll(placeholder, token));
        mission.setFailText(mission.getFailText().replaceAll(placeholder, token));
    }
}