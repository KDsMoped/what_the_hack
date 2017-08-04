package de.hsd.hacking.Entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.annotations.Expose;

/**
 * Parent class of all visual Actors in the game.
 * @author Florian
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
    }

    public Vector2 getPositionReference(){
        return position;
    }

    public void setPosition(Vector2 position) {
        super.setPosition(position.x, position.y);
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

    @Override
    public void act(float delta) {
        super.act(delta);
        position.set(getX(), getY());
    }
}
