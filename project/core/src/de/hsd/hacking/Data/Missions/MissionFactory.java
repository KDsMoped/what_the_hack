package de.hsd.hacking.Data.Missions;

import java.util.List;

import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Utils.RandomUtils;

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
    public static final Mission CreateRandomMission() {
        Mission mission = DataLoader.getInstance().getNewMission();
        mission.setDuration(2 + RandomUtils.randomInt(5));
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
    public static final Mission CreateRandomMission(int difficulty) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        RandomSkillValues(mission, difficulty);

        return mission;
    }

    /**
     * Create a new random mission object but choose the outcome on your own.
     * @param outcome
     * @return Random mission with defined outcome.
     */
    public static final Mission CreateRandomMission(MissionOutcome outcome) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        mission.setOutcome(outcome);

        return mission;
    }

    /**
     * Create a new random mission object but choose the skills on your own.
     * @param skills
     * @return Random mission with defined skills.
     */
    public static final Mission CreateRandomMission(List<Skill> skills) {
        de.hsd.hacking.Data.Missions.Mission mission = CreateRandomMission();
        mission.setSkill(skills);

        return mission;
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
    private static final void RandomSkillValues(Mission mission, int difficulty) {
        int min = 1;
        if (mission.getDifficulty() > 2) {
            min = difficulty - 1;
        }

        int max = difficulty + 2;

        for (Skill s:mission.getSkill()) {
            s.setValue(min + RandomUtils.randomInt(max + 1));
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