package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class MovingState extends EmployeeState {

    private static final float MAX_SPEED = 40f;


    private Vector2 destinationPos;
    private Tile endTile;
    private Path path;

    private float acceleration;
    private float speed;
    private boolean lastTile;
    private boolean delegating = false;
    private Interactable delegatingInteractable;


    public MovingState(Employee employee, Tile destinationTile) {
        super(employee);
        this.speed = 0f;
        this.acceleration = 20f;
        Tile currentTile = employee.getMovementProvider().getTileWhileMoving(employee.getPosition().add(Constants.TILE_WIDTH / 2f, Constants.TILE_WIDTH / 4f));

        //Remove employee from tile
        currentTile.setEmployee(null);
        this.lastTile = false;
        if (destinationTile != null){
            this.endTile = destinationTile;
            if (destinationTile.hasInteractableObject() && ((Interactable)destinationTile.getObject()).isDelegatingInteraction()){
                this.delegating = true;
                this.delegatingInteractable = ((Interactable)destinationTile);
            }else{
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
    }

    public MovingState(Employee employee){
        super(employee);
        this.speed = 0f;
        this.acceleration = 20f;
        Tile destinationTile = employee.getMovementProvider().getNextTile();
        Tile currentTile = employee.getMovementProvider().getTileWhileMoving(employee.getPosition().add(Constants.TILE_WIDTH / 2f, Constants.TILE_WIDTH / 4f));

        //Remove employee from tile
        currentTile.setEmployee(null);
        this.lastTile = false;

        if (destinationTile != null){
            this.endTile = destinationTile;
            if (destinationTile.hasInteractableObject() && ((Interactable)destinationTile.getObject()).isDelegatingInteraction()) {
                this.delegating = true;
                this.delegatingInteractable = ((Interactable) destinationTile);
            }else{
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
    }

    @Override
    EmployeeState act(float deltaTime) {
        if (!canceled) {
            if (delegating) {
                return this.delegatingInteractable.interact(employee);
            }
            // If destination wasn't reached yet, move further.
            if (destinationPos.cpy().sub(employee.getPosition()).len2() > 1f) {
                //EASE-IN / EASE-OUT
                if (!lastTile) {
                    speed += acceleration * deltaTime;
                    speed = MathUtils.clamp(speed, 0, MAX_SPEED);
                } else {
                    speed -= acceleration / 2f * deltaTime;
                    speed = MathUtils.clamp(speed, 10f, MAX_SPEED);
                }

                employee.setPosition(employee.getPosition().cpy().add(destinationPos.cpy().sub(employee.getPosition()).nor().scl(speed).scl(deltaTime)));
                return null;
            } else {
                if (path == null || path.isPathFinished()) {
                    employee.setPosition(destinationPos.cpy());

                    Tile pos = employee.getMovementProvider().getDiscreteTile(destinationPos.cpy());
                    //If there's an object, and it can be interacted with ->interact with it
                    Object obj = pos.getObject();
                    if (pos.hasInteractableObject() && !((Interactable) obj).isOccupied()) {
                        return ((Interactable) obj).interact(employee);
                    }

                    return new IdleState(employee);
                } else {
                    setNextDestination();
                    return null;
                }
            }
        }else{
            return new IdleState(employee);
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
        employee.resetElapsedTime();
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Moving State");
        employee.setAnimationState(Employee.AnimState.MOVING);
    }

    @Override
    void leave() {

    }

    @Override
    void cancel() {
        if (employee.equals(endTile.getEmployee())){
            endTile.setEmployee(null);
        }
    }
}
