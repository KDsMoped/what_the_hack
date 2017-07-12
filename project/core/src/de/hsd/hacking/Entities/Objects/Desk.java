package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Desk extends ContainerObject {

    private TextureRegion desk;

    public Desk(Assets assets, Direction direction, int occupyAmount) {
        super(null, true, false, false, direction, occupyAmount, new Vector2(0f, - Constants.TILE_WIDTH / 2f));
        desk = assets.getRandomDesk();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(desk, getPosition().x + getDrawPosAdjust().x, getPosition().y + getDrawPosAdjust().y);
        super.draw(batch, parentAlpha);
    }
}
