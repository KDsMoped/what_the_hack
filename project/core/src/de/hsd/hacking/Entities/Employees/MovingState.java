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
    private Tile nextTile;
    private Tile currentTile;
    private Path path;

    private float acceleration;
    private float speed;
    private boolean lastTile;
    private boolean delegating = false;
    private Interactable delegatingInteractable;


    public MovingState(Employee employee, Tile destinationTile) {
        super(employee);
        init(destinationTile);
    }


    public MovingState(Employee employee) {
        super(employee);
        Tile destinationTile = employee.getMovementProvider().getNextTile();
        init(destinationTile);
    }

    private void init(Tile destinationTile) {
        this.speed = 0f;
        this.acceleration = 20f;
        this.currentTile = employee.getMovementProvider().getTile(employee.getCurrentTileNumber());


        this.lastTile = false;

        if (destinationTile != null) {
            this.endTile = destinationTile;

            //If object at the end of path delegates to another object, walk there instead
            if (endTile.hasInteractableObject() && ((Interactable) endTile.getObject()).isDelegatingInteraction()) {
                this.delegating = true;
                this.delegatingInteractable = ((Interactable) endTile);
            } else {
                this.path = employee.getMovementProvider().getPathToTile(this.currentTile, this.endTile);

                if (path != null && !path.isPathFinished()) {
                    this.endTile.setOccupyingEmployee(employee);
                    setNextDestination();
                } else {
//                    currentTile.setOccupyingEmployee(employee);
                    this.destinationPos = currentTile.getPosition().cpy();
                }
            }
        } else {
            if (Constants.DEBUG) {
                Gdx.app.log(Constants.TAG, "No destination tile for employee" + employee.getName() + "found");
            }

            //No destination -> Current tile is destination;
            this.endTile = currentTile;
            this.endTile.setOccupyingEmployee(employee);
            this.destinationPos = endTile.getPosition().cpy();
        }
    }

    @Override
    EmployeeState act(float deltaTime) {
        if (!canceled) {
            if (delegating) {
                if (Constants.DEBUG) {
                    Gdx.app.log(Constants.TAG, employee.getName() + "Delegated by interactable object");
                }
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
                //Movemenet
                employee.setPosition(employee.getPosition().cpy().add(destinationPos.cpy().sub(employee.getPosition()).nor().scl(speed).scl(deltaTime)));
                return null;
            } else {
                //Path fully walked
                if (path == null || path.isPathFinished()) {
                    return finishPath();
                } else {
                    setNextDestination();
                    return null;
                }
            }
        } else {
            return new IdleState(employee);
        }
    }

    private EmployeeState finishPath() {

        //Set to endTile
        switchCurrentAndNextTiles(currentTile, endTile);

        //If there's an object, and it can be interacted with ->interact with it
        Object obj = this.endTile.getObject();
        if (this.endTile.hasInteractableObject() && !((Interactable) obj).isOccupied()) {
            if (Constants.DEBUG) {
                Gdx.app.log(Constants.TAG, "MovingState ended at interactable object for employee" + employee.getName());
            }
            return ((Interactable) obj).interact(employee);
        }
        if (Constants.DEBUG) {
            Gdx.app.log(Constants.TAG, "MovingState FINISHED for employee" + employee.getName());
        }
        return new IdleState(employee);
    }

    /**
     * Sets the next destination in the given path
     */
    private void setNextDestination() {

        switchCurrentAndNextTiles(currentTile, nextTile);

        this.nextTile = path.getNextStep();
        this.destinationPos = nextTile.getPosition().cpy();
        employee.flipHorizontal(destinationPos.x > employee.getPosition().x);
        if (path.isPathFinished()) {
            lastTile = true;
        }

    }

    @Override
    public void enter() {
        employee.resetElapsedTime();
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Moving State");
        employee.setAnimationState(Employee.AnimState.MOVING);
    }

    @Override
    void leave() {
        if (!employee.equals(employee.getMovementProvider().getTile(employee.getOccupiedTileNumber()).getOccupyingEmployee())) {
            throw new IllegalStateException("Employee not registered on tile he references in occupiedTileNumber when leaving MovingState. Employee: " + employee.toString());
        }
    }

    @Override
    public void cancel() {
        if (Constants.DEBUG) {
            Gdx.app.log(Constants.TAG, "MovingState was cancelled for employee " + employee.getName());
        }
        canceled = true;

        if (endTile != null) {
            endTile.setOccupyingEmployee(employee);
        }
        switchCurrentAndNextTiles(this.currentTile, this.nextTile);

    }

    private void switchCurrentAndNextTiles(Tile currentTile, Tile nextTile) {
        if (currentTile != null && nextTile != null) {
            this.currentTile = nextTile;
            this.currentTile.addEmployeeToDraw(employee);
        }
    }

    @Override
    public String getDisplayName() {
        return "Walking";
    }
}
