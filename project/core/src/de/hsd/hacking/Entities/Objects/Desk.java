package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Desk extends ContainerObject {

    private TextureRegion desk;

    public Desk(Assets assets, Direction direction, int occupyAmount) {
        super(null, true, false, false, direction, occupyAmount);
        desk = assets.getRandomDesk();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getDrawPosition() != null){
            batch.draw(desk, getDrawPosition().x , getDrawPosition().y);
        }else{
            batch.draw(desk, getPosition().x , getPosition().y);
        }

        super.draw(batch, parentAlpha);
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        setDrawPosition(position.cpy().sub(0f, Constants.TILE_WIDTH / 2f));
    }
}
