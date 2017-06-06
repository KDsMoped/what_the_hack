package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.hsd.hacking.Assets.Assets;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Lamp extends Object {

    private final Assets assets;

    public Lamp(Assets assets) {
        super(true, true);
        this.assets = assets;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(assets.lamp, getPosition().x, getPosition().y);
    }
}
