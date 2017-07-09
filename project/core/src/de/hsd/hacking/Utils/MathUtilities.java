package de.hsd.hacking.Utils;

import com.badlogic.gdx.math.MathUtils;

public class MathUtilities {

    /**
     * Calculates a linearly distributed value between -v and v.
     *
     * @param v
     * @return
     */
    public static int var(int v) {
        return RandomUtils.randomIntWithin(-v, v);
    }

    /**
     * Calculates a linearly distributed value between -v and v.
     *
     * @param v
     * @return
     */
    public static float var(float v) {
        return MathUtils.random(-v, v);
    }

    /**
     * Calculates a linearly distributed value between 1 - v and 1 + v.
     *
     * @param v
     * @return
     */
    public static float mult_var(float v) {
        return 1 + MathUtils.random(-v, v);
    }

    /**
     * Calculates a triangularly distributed value between 1 - v and 1 + v.
     * @param v
     * @return
     */
    public static float mult_var_trian(float v) {
        return 1 + MathUtils.randomTriangular(1 - v, 1 + v, 1);

    }
}
