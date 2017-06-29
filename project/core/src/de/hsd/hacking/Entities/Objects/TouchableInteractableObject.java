package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Utils.Direction;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public abstract class TouchableInteractableObject extends TouchableObject implements Interactable {

    private boolean occupied;
    private Direction facingDirection;

    public TouchableInteractableObject(TextureRegion region, boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(region, blocking, true, occupyDirection, occupyAmount);
        this.occupied = false;
        this.facingDirection = facingDirection;
    }

    @Override
    public boolean isOccupied(){
        return occupied;
    }

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
