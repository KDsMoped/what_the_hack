package de.hsd.hacking.Entities;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.annotations.Expose;

import java.util.Comparator;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public abstract class Entity extends Actor {

    @Expose
    private Vector2 position;
    private GameStage gameStage;
    private boolean blocking;

    public Entity(GameStage stage, boolean blocking){
        this.gameStage = stage;
        this.blocking = blocking;
        this.position = new Vector2();
    }

    @Override
    public abstract String getName();

    public Vector2 getPosition() {
        return position.cpy();
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public boolean isBlocking() {
        return blocking;
    }
}
