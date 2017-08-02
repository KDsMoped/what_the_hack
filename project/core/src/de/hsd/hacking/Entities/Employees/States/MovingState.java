package de.hsd.hacking.Entities.Employees.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Entities.Objects.InteractableObject;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Utils.Constants;

public class MovingState extends EmployeeState {

    private Tile nextTile;
    private Tile currentTile;
    private Tile endTile;
    private Path path;

    private boolean delegating = false;
    private boolean directedMovement = false;
    private Interactable delegatingInteractable;

    private boolean moving;


    public MovingState(Employee employee, Tile destinationTile) {
        super(employee);
        directedMovement = true;
        init(destinationTile);
    }


    public MovingState(Employee employee) {
        super(employee);
        Tile destinationTile = employee.getMovementProvider().getNextTile();
        init(destinationTile);
    }

    private void init(Tile destinationTile) {
        this.endTile = destinationTile;
        this.currentTile = employee.getMovementProvider().getTile(employee.getCurrentTileNumber());
        if (destinationTile != null) {
            //If object at the end of path delegates to another object, walk there instead
            if (destinationTile.hasInteractableObject() && ((Interactable) destinationTile.getObject()).isDelegatingInteraction()) {
                this.delegating = true;
                this.delegatingInteractable = ((Interactable) destinationTile);
            } else {
                this.path = employee.getMovementProvider().getPathToTile(this.currentTile, destinationTile);
                destinationTile.setOccupyingEmployee(employee);
            }
        } else {
            if (Constants.DEBUG) {
                Gdx.app.log(Constants.TAG, "No destination tile for employee" + employee.getName() + "found");
            }
        }
    }

    @Override
    public EmployeeState act(float deltaTime) {
        if (delegating) {
            if (Constants.DEBUG) {
                Gdx.app.log(Constants.TAG, employee.getName() + "Delegated by interactable object");
            }
            return this.delegatingInteractable.interact(employee);
        }
        if (path != null) {
            if (!path.isPathFinished()) {
                if (!moving) {
                    setNextDestination();
                    moving = true;
                    employee.addAction(Actions.sequence(Actions.moveTo(nextTile.getPosition().x, nextTile.getPosition().y, .5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                moving = false;
                            }
                        })
                    ));
                }
                return null;
            }
            if (!moving) {
                return finishPath();
            }
            return null;
        } else {
            return new IdleState(employee);
        }
    }

    /**
     * Ends movement and registers employee on tile. Interacts with object if object permits it.
     * @return next state for employee
     */
    private EmployeeState finishPath() {

        //Set to endTile
        switchCurrentAndNextTiles(currentTile, nextTile);

        //If there's an object, and it can be interacted with ->interact with it
        Object obj = this.currentTile.getObject();
        if (this.currentTile.hasInteractableObject() && !((Interactable) obj).isOccupied()
                && (directedMovement || ((InteractableObject) obj).isAllowRandomInteraction())) {
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
        employee.flipHorizontal(nextTile.getPosition().x > employee.getPosition().x);
    }

    @Override
    public void enter() {
        employee.resetElapsedTime();
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Moving State");
        employee.setAnimationState(Employee.AnimState.MOVING);
    }

    @Override
    public void leave() {

        if (!employee.equals(employee.getMovementProvider().getTile(employee.getOccupiedTileNumber()).getOccupyingEmployee())) {
            throw new IllegalStateException("Employee not registered on tile he references in occupiedTileNumber when leaving MovingState. Employee: " + employee.toString());
        }
    }

    @Override
    public void cancel() {
        if (Constants.DEBUG) {
            Gdx.app.log(Constants.TAG, "MovingState was cancelled for employee " + employee.getName());
        }
        //Cleanup
        employee.clearActions();
        if (this.endTile.hasInteractableObject()){
            ((InteractableObject) this.endTile.getObject()).deOccupy();
        }
        employee.setState(new IdleState(employee));

    }

    private void switchCurrentAndNextTiles(final Tile currentTile, final Tile nextTile) {
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
