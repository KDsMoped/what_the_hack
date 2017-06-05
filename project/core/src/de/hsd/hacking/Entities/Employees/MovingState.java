package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class MovingState extends EmployeeState {

    private static final float MAX_SPEED = 40f;


    private Vector2 destinationPos;
    private Path path;

    private float acceleration;
    private float speed;
    private boolean lastTile;


    public MovingState(Employee employee) {
        super(employee);
        this.speed = 0f;
        this.acceleration = 20f;
        Tile currentTile = employee.getMovementProvider().getTile(employee.getPosition().cpy().add(1f, 1f));
        Tile destinationTile = employee.getMovementProvider().getNextTile();

        //Remove employee from tile
        currentTile.setEmployee(null);
        this.lastTile = false;
        if (destinationTile != null){
            this.path = employee.getMovementProvider().getPathToTile(currentTile, destinationTile);
            destinationTile.setEmployee(employee);
            if (path != null && !path.isPathFinished()){
                setNextDestination();
            }else{
                destinationTile = currentTile;
                this.destinationPos = destinationTile.getPosition();
            }
        }
    }

    @Override
    EmployeeState act(float deltaTime) {
        // If destination wasn't reached yet, move further.
        if (destinationPos.cpy().sub(employee.getPosition()).len() > 0.5f){
            //EASE-IN / EASE-OUT
            if (!lastTile){
                speed += acceleration * deltaTime;
                speed = MathUtils.clamp(speed, 0 , MAX_SPEED);
            }else{
                speed -= acceleration /2f * deltaTime;
                speed = MathUtils.clamp(speed, 10f, MAX_SPEED);
            }

            employee.setPosition(employee.getPosition().cpy().add(destinationPos.cpy().sub(employee.getPosition()).nor().scl(speed).scl(deltaTime)));
            return null;
        }else{
            if (path == null || path.isPathFinished()){
                return new IdleState(employee);
            }else{
                setNextDestination();

                return null;
            }
        }
    }

    /**
     * Sets the next destination in the given path
     */
    private void setNextDestination(){
        Tile nextTile = path.getNextStep();
        this.destinationPos = nextTile.getPosition().cpy();
        employee.flipHorizontal(destinationPos.x > employee.getPosition().x);
        if (path.isPathFinished()){
            lastTile = true;
        }

    }

    @Override
    public void enter() {
        super.enter();
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Moving State");
        employee.setAnimationState(Employee.AnimState.MOVING);
    }

    @Override
    void leave() {

    }
}
