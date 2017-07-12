package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Entity;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public abstract class Object extends Entity {

    protected TextureRegion drawableRegion;
    private Direction occupyDirection;
    private int occupyAmount;

    public Object(TextureRegion region, boolean blocking, boolean touchable, boolean interactable, Direction occupyDirection, int occupyAmount) {
        super(blocking, touchable, interactable);
        this.drawableRegion = region;
        this.occupyDirection = occupyDirection;
        this.occupyAmount = occupyAmount;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (drawableRegion != null){
            batch.draw(drawableRegion, getPosition().x, getPosition().y);
        }
    }

    @Override
    public String getName() {
        return "";
    }

    public void setDrawableRegion(TextureRegion drawableRegion) {
        this.drawableRegion = drawableRegion;
    }

    public Direction getOccupyDirection() {
        return occupyDirection;
    }

    public int getOccupyAmount() {
        return occupyAmount;
    }


}
