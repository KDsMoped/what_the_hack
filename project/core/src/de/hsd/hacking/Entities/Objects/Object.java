package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public abstract class Object extends Entity {

    private boolean repositionable;
    private TextureRegion drawableRegion;
    private Direction occupyDirection;
    private int occupyAmount;

    public Object(TextureRegion region, boolean blocking, boolean repositionable, Direction occupyDirection, int occupyAmount) {
        super(blocking);
        this.drawableRegion = region;
        this.repositionable = repositionable;
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

    public boolean isRepositionable(){
        return repositionable;
    }

    public void setDrawableRegion(TextureRegion drawableRegion) {
        this.drawableRegion = drawableRegion;
    }
}
