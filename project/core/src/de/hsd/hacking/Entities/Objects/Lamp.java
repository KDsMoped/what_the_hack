package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Lamp extends Object {

    private final Assets assets;

    public Lamp(Assets assets) {
        super(assets.lamp, true, true, Direction.DOWN, 0);
        this.assets = assets;
    }
}
