package de.hsd.hacking.UI.General;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public abstract class Button extends Actor {

    private boolean disabled = false;
    protected boolean pressed = false;
    private Array<TextureRegion> regions;

    protected Rectangle bounds;

    public Button(Vector2 position, Array<TextureRegion> buttonRegions){
        this.regions = buttonRegions;
        this.bounds = new Rectangle(position.x - buttonRegions.get(0).getRegionWidth() / 2f, position.y,
                buttonRegions.get(0).getRegionWidth(), buttonRegions.get(0).getRegionHeight());
    }

    public boolean touchDown(Vector2 coords){
        if (!this.disabled && this.bounds.contains(coords)){
            this.pressed = true;
            return true;
        }else{
            return false;
        }
    }
    public boolean touchUp(Vector2 coords){
        if(this.pressed && bounds.contains(coords)){
            this.pressed = false;
            return true;
        }
        this.pressed = false;
        return false;
    }

    public void setDisabled(boolean disabled){
        this.disabled = disabled;
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(disabled){
            batch.setColor(.5f, .5f, .5f, .5f);
            batch.draw(regions.get(0), bounds.x, bounds.y, bounds.width, bounds.height);
            batch.setColor(1f, 1f, 1f, 1f);
        }else{
            batch.draw(pressed? regions.get(1) : regions.get(0), bounds.x, bounds.y, bounds.width, bounds.height);
        }

    }
}
