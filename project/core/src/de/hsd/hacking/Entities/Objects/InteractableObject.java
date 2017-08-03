package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Utils.Direction;

public abstract class InteractableObject extends Object implements Interactable {

    private boolean occupied;
    private Direction facingDirection;
    private boolean allowRandomInteraction;

    public InteractableObject(TextureRegion region, boolean blocking, boolean touchable,
                              boolean interactable, Direction occupyDirection, int occupyAmount,
                              Direction facingDirection, boolean allowRandomInteraction) {
        super(region, blocking, touchable, interactable, occupyDirection, occupyAmount);
        occupied = false;
        this.allowRandomInteraction = allowRandomInteraction;
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

    public boolean isAllowRandomInteraction() {
        return allowRandomInteraction;
    }
}
