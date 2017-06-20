package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Entities.Direction;

/**
 * Created by Cuddl3s on 18.06.2017.
 */

public abstract class InteractableObject extends Object implements Interactable {

    private boolean occupied;
    private Direction facingDirection;

    public InteractableObject(TextureRegion region, boolean blocking, boolean touchable, boolean interactable, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(region, blocking, touchable, interactable, occupyDirection, occupyAmount);
        occupied = false;
        this.facingDirection = facingDirection;
    }

    public boolean isOccupied(){
        return occupied;
    }

    @Override
    public abstract void occupy();

    protected void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    @Override
    public boolean isBlocking() {
        return super.isBlocking() || isOccupied();
    }

    @Override
    public Direction getFacingDirection() {
        return facingDirection;
    }
}
