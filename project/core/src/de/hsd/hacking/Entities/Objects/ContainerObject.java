package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Direction;

public abstract class ContainerObject extends Object {

    private Object[] containedObjects;
    private Vector2 drawPosAdjust;
    private int maxContainedObjects;


    public ContainerObject(TextureRegion drawableRegion, boolean blocking, boolean touchable, boolean interactable, Direction occupyDirection, int occupyAmount, Vector2 drawPosAdjust) {
        super(drawableRegion, blocking, touchable, interactable, occupyDirection, occupyAmount);
        maxContainedObjects = occupyAmount + 1;
        containedObjects = new Object[maxContainedObjects];
        this.drawPosAdjust = drawPosAdjust.cpy();
    }

    /**
     * Checks if a slot is empty.
     * @param slot the desired slot. 0 is tile the containerobject stands on, incrementing by one in {@link Direction} occupyDirection
     * @return 1 if emtpy, 0 if occupied, -1 if slot parameter is not valid
     */
    public int isEmpty(int slot){
        if (slot >= maxContainedObjects || slot < 0){
            return -1;
        }
        return containedObjects[slot] == null ? 1 : 0;
    }

    public Object getContainedObject(int slot){
        if (slot < 0 || slot >= maxContainedObjects) return null;
        return containedObjects[slot];
    }

    public void setContainedObject(final Object containedObject, int slot) {
        if (slot < 0 || slot >= maxContainedObjects || containedObjects[slot] != null){
            Gdx.app.error(Constants.TAG, "setContainedObject called with illegal slot parameter: " + slot);
            return;
        }
        containedObjects[slot] = containedObject;
        containedObject.setPosition(getDrawPosition(slot));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < containedObjects.length; i++) {
            if (isEmpty(i) == 0){
                containedObjects[i].draw(batch, parentAlpha);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (int i = 0; i < containedObjects.length; i++) {
            if (isEmpty(i) == 0) {
                containedObjects[i].act(delta);
            }
        }
    }

    public Vector2 getDrawPosition(int slot) {
        int yAdjust = 0;
        int xAdjust = 0;
        switch (getOccupyDirection()){
            case UP:
                yAdjust = 1;
                xAdjust = 1;
                break;
            case RIGHT:
                xAdjust = 1;
                yAdjust = -1;
                break;
            case DOWN:
                yAdjust = -1;
                xAdjust = -1;
                break;
            case LEFT:
                xAdjust = -1;
                yAdjust = 1;
        }
        return getPosition().add(drawPosAdjust).add(slot * xAdjust * Constants.TILE_WIDTH , slot * yAdjust * Constants.TILE_WIDTH / 4f);
    }

    public Vector2 getDrawPosAdjust(){
        return drawPosAdjust;
    }

}
