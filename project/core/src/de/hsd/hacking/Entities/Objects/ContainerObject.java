package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Utils.Direction;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public abstract class ContainerObject extends Object {

    private Object containedObject;
    private Vector2 drawPosition;


    public ContainerObject(TextureRegion drawableRegion, boolean blocking, boolean touchable, boolean interactable, Direction occupyDirection, int occupyAmount) {
        super(drawableRegion, blocking, touchable, interactable, occupyDirection, occupyAmount);
    }

    public boolean isEmpty(){
        return containedObject == null;
    }

    public Object getContainedObject(){
        return containedObject;
    }

    public void setContainedObject(Object containedObject) {
        this.containedObject = containedObject;
        this.containedObject.setPosition(getDrawPosition());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isEmpty()){
            containedObject.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isEmpty()){
            containedObject.act(delta);
        }
    }

    public Vector2 getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(Vector2 drawPosition) {
        this.drawPosition = drawPosition;
    }
}
