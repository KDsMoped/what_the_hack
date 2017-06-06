package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Desk extends ContainerObject {

    private TextureRegion desk;

    public Desk(Assets assets, int variant) {
        super(null, true, true);
        switch (variant){
            case 0:
                desk = assets.desk_1;
                break;
            case 1:
                desk = assets.desk_2;
                break;
            default:
                desk = assets.desk_2;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(desk, getPosition().x, getPosition().y - Constants.TILE_WIDTH / 2f);
        super.draw(batch, parentAlpha);
    }
}
