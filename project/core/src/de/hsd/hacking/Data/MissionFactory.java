package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class contains methods to generate various mission objects.
 */
public final class MissionFactory {
    public static final Mission CreateRandomMission() {
        Mission mission = DataLoader.getInstance().getNewMission();


        return mission;
    }

    public static final Mission CreateRandomMission(int difficulty) {
        Mission mission = CreateRandomMission();
        mission.setDifficulty(difficulty);

        return mission;
    }

    public static final Mission CreateRandomMission(MissionOutcome outcome) {
        Mission mission = CreateRandomMission();
        mission.setOutcome(outcome);

        return mission;
    }

    public static final Mission CreateRandomMission(List<Skill> skills) {
        Mission mission = CreateRandomMission();
        mission.setSkill(skills);

        return mission;
    }

    private static final List<Skill> RandomSkills() {
        List<Skill> skillz = new ArrayList<Skill>();

        for (int i = 0; i < 3; i++) {
            Skill skill = new Skill(SkillType.getRandomSkill(), 9);
            skillz.add(skill);
        }

        return skillz;
    }
}
