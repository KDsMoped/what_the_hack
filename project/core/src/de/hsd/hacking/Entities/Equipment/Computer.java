package de.hsd.hacking.Entities.Equipment;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Computer extends Equipment {

    private TextureRegion stillRegion;
    private Animation animation;

    public Computer(float price, EquipmentAttributeLevel attributeLevel) {
        super(price, EquipmentAttributeType.COMPUTATIONPOWER, attributeLevel, true);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
