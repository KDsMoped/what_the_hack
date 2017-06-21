package de.hsd.hacking.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Mission;
import de.hsd.hacking.Data.MissionFactory;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 14.06.17.
 */

public class EmployeeProfile extends Popup {
    public EmployeeProfile(Assets assets) {
        super(assets);

        Mission m = MissionFactory.CreateRandomMission();
        Gdx.app.log(Constants.TAG, m.getName());
        Gdx.app.log(Constants.TAG, m.getDescription());
    }

    @Override
    public void act(float delta) {
        if (!isActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }
}
