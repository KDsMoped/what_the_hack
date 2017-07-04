package de.hsd.hacking.Entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.annotations.Expose;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public abstract class Entity extends Actor {

    @Expose
    private Vector2 position;
    private boolean blocking;
    private boolean touchable;
    private boolean interactable;

    public Entity(boolean blocking, boolean touchable, boolean interactable){
        this.blocking = blocking;
        this.position = new Vector2();
        this.touchable = touchable;
        this.interactable = interactable;
    }

    @Override
    public abstract String getName();

    public Vector2 getPosition() {
        return new Vector2(super.getX(), super.getY());
//        return position.cpy();
    }

    public void setPosition(Vector2 position) {
        super.setPosition(position.x, position.y);
//        this.position.set(position);
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    public boolean isInteractable() {
        return interactable;
    }
}
