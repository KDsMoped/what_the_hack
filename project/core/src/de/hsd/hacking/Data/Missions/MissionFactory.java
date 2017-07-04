package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Data.CompanyNamesHolder;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class contains methods to generate various mission objects.
 */
public final class MissionFactory {
    /**
     * Create a new random mission object. Name and description are read
     * @return random Mission
     */
    public static final de.hsd.hacking.Data.Missions.Mission CreateRandomMission() {
        de.hsd.hacking.Data.Missions.Mission mission = DataLoader.getInstance().getNewMission();
        mission.setDuration(MathUtils.random(4) + 2);
        mission.setSkill(RandomSkills());
        RandomSkillValues(mission, mission.getDifficulty());
        mission.setOutcome(RandomOutcome());
        ReplacePlaceholders(mission);

        return mission;
    }

    /**
     * Create a new random mission object with the required skill values based on the given difficulty.
     * @param difficulty
     * @return Random mission with defined difficulty.
     */
    public static final de.hsd.hacking.Data.Missions.Mission CreateRandomMission(int difficulty) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        RandomSkillValues(mission, difficulty);

        return mission;
    }

    /**
     * Create a new random mission object but choose the outcome on your own.
     * @param outcome
     * @return Random mission with defined outcome.
     */
    public static final de.hsd.hacking.Data.Missions.Mission CreateRandomMission(MissionOutcome outcome) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        mission.setOutcome(outcome);

        return mission;
    }

    /**
     * Create a new random mission object but choose the skills on your own.
     * @param skills
     * @return Random mission with defined skills.
     */
    public static final de.hsd.hacking.Data.Missions.Mission CreateRandomMission(List<Skill> skills) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        mission.setSkill(skills);

        return mission;
    }

    /**
     * Generates a list of 3 random skills with 3 random values.
     * @return List of 3 random skills
     */
    private static final List<Skill> RandomSkills() {
        List<Skill> skillz = new ArrayList<Skill>();

        for (int i = 0; i < 3; i++) {
            Skill skill = new Skill(SkillType.getRandomSkill(false), MathUtils.random(9) + 1);
            skillz.add(skill);
        }

        return skillz;
    }

    /**
     * Creates a new MissionOutcome object with random outcome.
     * @return Random mission outcome object.
     */
    private static final MissionOutcome RandomOutcome() {
        MissionOutcome outcome = new MissionOutcome();

        return outcome;
    }

    /**
     * Gives the skills required for this mission random values based on the given difficulty.
     * @param mission The mission that needs new skill values.
     */
    private static final void RandomSkillValues(de.hsd.hacking.Data.Missions.Mission mission, int difficulty) {
        int min = 1;
        if (mission.getDifficulty() > 2) {
            min = difficulty - 1;
        }

        int max = difficulty + 2;

        for (Skill s:mission.getSkill()) {
            s.setValue(MathUtils.random(min, max));
        }
    }

    /**
     * Replaces the %COMPANY% tag in mission data with random company names.
     * @param mission
     */
    private static final void ReplacePlaceholders(Mission mission) {
        String company = DataLoader.getInstance().getNewCompanyName();
        mission.setName(mission.getName().replaceAll("%COMPANY%", company));
        mission.setDescription(mission.getDescription().replaceAll("%COMPANY%", company));
    }
}