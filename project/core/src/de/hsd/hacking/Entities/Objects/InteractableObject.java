package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Utils.Direction;

/**
 * Abstract class of an object that employees can interact with.
 * @author Florian
 */
public abstract class InteractableObject extends Object implements Interactable {

    /**
     * Whether the object is considered occupied and can therefore not be interacted with until deoccupied.
     */
    private boolean occupied;
    /**
     * Returns the direction this object faces for animation purposes.
     */
    private Direction facingDirection;
    /**
     * Whether an employee should be allowed to interact with the object when he reaches the tile it stands on by chance.
     */
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
