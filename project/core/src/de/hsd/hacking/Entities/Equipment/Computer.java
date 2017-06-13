package de.hsd.hacking.Entities.Equipment;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Touchable;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Computer extends Equipment implements Interactable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> animation;
    private boolean on;
    private float elapsedTime = 0f;


    public Computer(float price, EquipmentAttributeLevel attributeLevel, Assets assets) {
        super(assets.computer.get(0), price, EquipmentAttributeType.COMPUTATIONPOWER, attributeLevel, true, Direction.DOWN, 0);
        this.stillRegion = assets.computer.get(0);
        this.animation = new Animation<TextureRegion>(.2f, assets.computer.get(1), assets.computer.get(2), assets.computer.get(3));

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        if (on){
            setDrawableRegion(animation.getKeyFrame(elapsedTime));
        }else{
            setDrawableRegion(stillRegion);
        }
    }

    @Override
    public void interact() {
        elapsedTime = 0f;
        on = !on;
    }

    @Override
    public void onTouch() {
        //TODO show stats etc...
    }
}
