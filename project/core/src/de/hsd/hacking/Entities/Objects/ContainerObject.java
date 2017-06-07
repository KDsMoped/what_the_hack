package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public abstract class ContainerObject extends Object {

    private Object containedObject;
    private TextureRegion container;

    public ContainerObject(TextureRegion container, boolean blocking, boolean repositionable) {
        super(blocking, repositionable);
        this.container = container;
    }

    public boolean isEmpty(){
        return containedObject == null;
    }

    public Object getContainedObject(){
        return containedObject;
    }

    public void setContainedObject(Object containedObject) {
        this.containedObject = containedObject;
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
}
