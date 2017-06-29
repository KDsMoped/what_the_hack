package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Direction;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Lamp extends Object {

    public Lamp(Assets assets) {
        super(assets.lamp, true, false, false, Direction.DOWN, 0);
    }
}
