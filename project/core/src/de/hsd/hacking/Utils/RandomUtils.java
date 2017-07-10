package de.hsd.hacking.Utils;

import com.badlogic.gdx.math.RandomXS128;

/**
 * Created by Cuddl3s on 05.07.2017.
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


}
