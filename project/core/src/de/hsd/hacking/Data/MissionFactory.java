package de.hsd.hacking.Data;

import java.util.List;

import de.hsd.hacking.Entities.Employees.Skill;

/**
 * Created by ju on 15.06.17.
 */

/**
 * This class contains methods to generate various mission objects.
 */
public final class MissionFactory {
    public final Mission CreateRandomMission() {
        Mission mission = new Mission();

        return mission;
    }

    public final Mission CreateRandomMission(int difficulty) {
        Mission mission = new Mission();

        return mission;
    }

    public final Mission CreateRandomMission(MissionOutcome outcome) {
        Mission mission = new Mission();

        return mission;
    }

    public final Mission CreateRandomMission(List<Skill> skills) {
        Mission mission = new Mission();

        return mission;
    }

    public final Mission CreateRandomMission(String name, String descrition) {
        Mission mission = new Mission();

        return mission;
    }

    /**
     * Choose a random name from a json list.
     */
    private final String RandomName() {
        String name = "";

        return name;
    }

    /**
     * Choose a random description from a json list.
     */
    private final String RandomDescription() {
        String description = "";

        return description;
    }
}
