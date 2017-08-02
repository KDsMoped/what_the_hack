package de.hsd.hacking.Utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;

import java.util.Collection;
import java.util.List;

/**
 * Various utility functions for randomness based on Java RandomXS128.
 *
 * @author Florian, Hendrik
 */
public class RandomUtils {

    private static RandomXS128 rand = new RandomXS128();

    public static int randomInt(){
        return rand.nextInt();
    }

    public static int randomInt(int excl) {
        return rand.nextInt(excl);
    }

    public static float randomFloat() {
        return rand.nextFloat();
    }

    /**
     * Returns an in within the interval from {@code from} (inclusive) and {@code to} (inclusive).
     * @param from
     * @param to
     * @return
     */
    public static int randomIntWithin(final int from, final int to){
    return rand.nextInt(to - from + 1) + from;
    }

    /**
     * Returns a random element from the given List.
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T randomElement(List<T> collection){
        return collection.get(randomInt(collection.size()));
    }

    /**
     * Calculates a linearly distributed value between -v and v.
     *
     * @param v
     * @return
     */
    public static int var(int v) {
        return randomIntWithin(-v, v);
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
