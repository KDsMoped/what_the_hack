package de.hsd.hacking.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class Tile extends Actor {


    private Vector2 position;
    private int tileNumber;
    private Employee employee;
    private Entity object;
    private List<Employee> passersBy;
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
        this.passersBy = new ArrayList<Employee>(4);
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

        if (Constants.DEBUG){
            batch.end();
            testRenderer.begin(ShapeRenderer.ShapeType.Filled);
            testRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            testRenderer.setTransformMatrix(batch.getTransformMatrix());
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            if (isMovableThrough()){
                if (employee == null){
                    if (passersBy.size() > 0){
                        testRenderer.setColor( Color.YELLOW.cpy().sub(0,0,0, .5f));
                    }else{
                        testRenderer.setColor( Color.RED.cpy().sub(0,0,0, .75f));
                    }
                }else{
                    testRenderer.setColor(Color.GREEN.cpy().sub(0,0,0, .75f));
                }
            }else{
                testRenderer.setColor(Color.BLUE.cpy().sub(0,0,0, .75f));
            }
            testRenderer.rect(position.x , position.y , bounds.width, bounds.height);
            testRenderer.end();
            batch.begin();
//            Assets.gold_font_small.draw(batch, "" + tileNumber , position.x + 10f, position.y + 12f);

        }
        if (object != null){
            object.draw(batch, parentAlpha);
        }
        if(employee != null && employee.getAnimationState() == Employee.AnimState.IDLE){ //TODO Quick fix, der Employee sollte nur gesetzt werden wenn er an Tile drankommt
            employee.draw(batch, parentAlpha);
        }
        if(passersBy.size() > 0){
            for (Employee empl : passersBy) {
                empl.draw(batch, parentAlpha);
            }
        }
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
        this.object.setPosition(this.position.cpy());
    }

    public void addPasserBy(Employee employee) {
        passersBy.add(employee);
    }
    public void clearPassersBy(){
        passersBy.clear();
    }
}
