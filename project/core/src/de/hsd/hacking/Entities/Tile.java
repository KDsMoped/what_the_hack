package de.hsd.hacking.Entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class Tile extends Actor {


    private Vector2 middlePosition;
    private int tileNumber;
    private Entity entity;

    private ShapeRenderer testRenderer;

    public Tile(Vector2 position, int tileNumber){
        this.tileNumber = tileNumber;
        middlePosition = position;
        testRenderer = new ShapeRenderer();
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isEmpty(){
        return entity == null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isEmpty()){
            entity.draw(batch, parentAlpha);
        }
        batch.end();
        testRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        testRenderer.setTransformMatrix(batch.getTransformMatrix());

        testRenderer.setColor(isEmpty() ? Color.RED : Color.GREEN);
        testRenderer.begin(ShapeRenderer.ShapeType.Filled);
        testRenderer.rect(middlePosition.x - 5f, middlePosition.y - 5f, 5f, 5f, 10f, 10f, 1f, 1f, 45f);
    }

    public Vector2 getMiddlePosition(){
        return middlePosition;
    }
}
