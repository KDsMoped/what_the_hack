package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public enum SkillType {

    Social, Hardware, Software, Network, Crypto, Search, All_Purpose;

    private static final SkillType[] VALUES = values();
    public static final int SIZE = VALUES.length;

    public static SkillType getRandomSkill() {
        return VALUES[MathUtils.random(SIZE - 1)];
    }

    public String getDisplayName() {
        switch (this) {
            case Crypto:
                return "Crypto";
            case Search:
                return "Search";
            case Social:
                return "Social";
            case Network:
                return "Network";
            case Hardware:
                return "Hardware";
            case Software:
                return "Software";
            case All_Purpose:
                return "Allrounder";
            default:
                return name();
        }
    }
}
