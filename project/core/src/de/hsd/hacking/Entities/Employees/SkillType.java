package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public enum SkillType {

    SOCIAL, HARDWARE, SOFTWARE, NETWORK, CRYPTO, SEARCH, ALLPURPOSE;

    private static final SkillType[] VALUES = values();
    public static final int SIZE = VALUES.length;

    public static SkillType getRandomSkill() { return VALUES[MathUtils.random(SIZE - 1)]; }
}
