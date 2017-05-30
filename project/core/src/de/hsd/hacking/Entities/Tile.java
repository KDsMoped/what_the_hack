package de.hsd.hacking.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class Tile extends Actor {


    private Vector2 position;
    private int tileNumber;
    private Employee employee;
    private Entity object;
    private Rectangle bounds;

    private ShapeRenderer testRenderer;

    /**
     * Instantiates a single tile in a tileGrid. Should not be called directly, rather use {@link IsometricTileManager} to create Tiles
     * @param position screen position of the tile
     * @param tileNumber index of the tile in the tile-grid
     * @param tileWidth width of the tile
     */
    public Tile(Vector2 position, int tileNumber, float tileWidth){
        this.tileNumber = tileNumber;
        this.position = position;
        testRenderer = new ShapeRenderer();
        this.bounds = new Rectangle(this.position.x, this.position.y, tileWidth, tileWidth / 2f);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isMovableTo(){
        return employee == null && (object == null || !object.isBlocking());
    }

    public boolean isMovableThrough(){
        return object == null || !object.isBlocking();
    }

    public boolean hasNoObject(){
        return object == null;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        testRenderer.begin(ShapeRenderer.ShapeType.Filled);
        testRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        testRenderer.setTransformMatrix(batch.getTransformMatrix());
        if (isMovableThrough()){
            testRenderer.setColor(employee == null ? Color.RED : Color.GREEN);
        }else{
            testRenderer.setColor(Color.BLUE);
        }

        testRenderer.rect(position.x , position.y , bounds.width, bounds.height);
        testRenderer.end();
        batch.begin();
    }

    public Vector2 getPosition(){
        return position;
    }

    public boolean isInTile(Vector2 position){
        return bounds.contains(position);
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public Entity getObject() {
        return object;
    }

    public void setObject(Entity object) {
        this.object = object;
    }
}
