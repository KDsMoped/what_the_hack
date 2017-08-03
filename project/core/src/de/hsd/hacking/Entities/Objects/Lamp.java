package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Direction;

public class Lamp extends Object {

    public Lamp(Assets assets) {
        super(assets.lamp, true, false, false, Direction.DOWN, 0);
    }
}
