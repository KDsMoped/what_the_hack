package de.hsd.hacking.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Utils.Constants;

public class Tile extends Actor {


    private Vector2 position;
    private int tileNumber;
    private Employee occupyingEmployee;
    private Object object;
    private List<Employee> employeesToDraw;
    private Rectangle bounds;

    private ShapeRenderer testRenderer;

    /**
     * Instantiates a single tile in a tileGrid. Should not be called directly, rather use {@link IsometricTileManager} to create Tiles
     * @param position screen position of the tile
     * @param tileNumber index of the tile in the tile-grid
     * @param tileWidth width of the tile
     */
    Tile(Vector2 position, int tileNumber, float tileWidth){
        this.tileNumber = tileNumber;
        this.position = position;
        testRenderer = new ShapeRenderer();
        this.bounds = new Rectangle(this.position.x, this.position.y, tileWidth, tileWidth / 2f);
        this.employeesToDraw = new ArrayList<Employee>(4);
    }

    public Employee getOccupyingEmployee() {
        return occupyingEmployee;
    }

    public void setOccupyingEmployee(final Employee newOccupyingEmployee) {
        //Should only be called when theres no employee on this tile
        if (this.occupyingEmployee == null) {
            //if the newly occupant is occupying another tile, remove him from there
            if (newOccupyingEmployee != null) {
                newOccupyingEmployee.removeFromOccupyingTile();
                newOccupyingEmployee.setOccupiedTileNumber(tileNumber);
            }
        }
        this.occupyingEmployee = newOccupyingEmployee;
    }

    public boolean isMovableTo(){
        return occupyingEmployee == null && (object == null || !object.isBlocking());
    }

    public boolean isMovableThrough(){
        return object == null || !object.isBlocking();
    }

    public boolean hasNoObject(){
        return object == null;
    }

    public boolean hasInteractableObject(){
        return object != null && object.isInteractable();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (Constants.DEBUG){
            drawDebug(batch);
        }

        if (object != null){
            object.draw(batch, parentAlpha);
        }
        /*if(occupyingEmployee != null && (occupyingEmployee.getAnimationState() != Employee.AnimState.MOVING)){
            occupyingEmployee.draw(batch, parentAlpha);
        }*/

        if(employeesToDraw.size() > 0){
            if (employeesToDraw.size() > 1){
                Collections.sort(employeesToDraw);
            }

            for (Employee empl : employeesToDraw) {
                empl.draw(batch, parentAlpha);
            }
        }
    }

    @Override
    public void act(float delta) {
        if (object != null){
            this.object.act(delta);
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
        this.object.setPosition(this.position.cpy());
    }

    public void addEmployeeToDraw(Employee employee) {
        //When employee isn't already drawn by this tile
        if (!employeesToDraw.contains(employee)){
            employee.removeFromDrawingTile();
            employeesToDraw.add(employee);
            employee.setCurrentTileNumber(tileNumber);
        }
    }

    public void clearEmployeesToDraw(){
        employeesToDraw.clear();
    }

    private void drawDebug(Batch batch) {
        batch.end();
        testRenderer.begin(ShapeRenderer.ShapeType.Filled);
        testRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        testRenderer.setTransformMatrix(batch.getTransformMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (isMovableThrough()){
            if (occupyingEmployee == null){
                if (employeesToDraw.size() > 0){
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
    }

    public boolean removeEmployeeToDraw(Employee employee) {
        return employeesToDraw.remove(employee);
    }

    public int getEmployeesToDrawSize(){
        return employeesToDraw.size();
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return obj instanceof Tile && ((Tile) obj).getTileNumber() == tileNumber;
    }
}
