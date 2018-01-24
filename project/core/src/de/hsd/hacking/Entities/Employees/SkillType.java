package de.hsd.hacking.Entities.Employees;

import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.RandomUtils;

public class SkillType {
    public Proto.SkillType skillType;

    private static final Proto.SkillType[] VALUES = Proto.SkillType.values();
    public static final int SIZE = VALUES.length;

    public SkillType(Proto.SkillType skillType) {
        this.skillType = skillType;
    }

    public static Proto.SkillType getRandomSkill(Boolean allPurpose) {
        if (allPurpose) {
            return VALUES[RandomUtils.randomInt(SIZE - 1)];
        }
        else {
            return VALUES[RandomUtils.randomInt(SIZE - 2)];
        }

    }

    public String getDisplayName() {
        switch (this.skillType) {
            case Crypto:
                return "Crypto";
            case Search:
                return "Data Analysis";
            case Social:
                return "Social";
            case Network:
                return "Network";
            case Hardware:
                return "Hardware";
            case Software:
                return "Software";
            case All_Purpose:
                return "Allround";
            default:
                return skillType.name();
        }
    }
}
