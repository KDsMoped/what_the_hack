package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.hsd.hacking.Assets.Assets;

/**
 * Created by ju on 14.06.17.
 */

public class EmployeeProfile extends Popup {
    public EmployeeProfile(Assets assets) {
        super(assets);
    }

    @Override
    public void act(float delta) {
        if (!getActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!getActive()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }
}
