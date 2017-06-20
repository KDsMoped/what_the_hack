package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class WorkingState extends EmployeeState {

    Vector2 workingPosition;

    public WorkingState(Employee employee, Vector2 position) {
        super(employee);
        this.workingPosition = position;
        employee.setPosition(position.cpy());
        employee.getMovementProvider().getDiscreteTile(position).setEmployee(employee);
    }

    @Override
    EmployeeState act(float deltaTime) {
        if (!isCanceled()){
            return null;
        }
        return new IdleState(employee);
    }

    @Override
    void enter() {
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Working State");
        employee.setAnimationState(Employee.AnimState.WORKING);
        Interactable workPlace = (Interactable) employee.getMovementProvider().getDiscreteTile(workingPosition).getObject();
        boolean left = workPlace.getFacingDirection() == Direction.LEFT || workPlace.getFacingDirection() == Direction.DOWN;
        boolean backFaced = workPlace.getFacingDirection() == Direction.UP || workPlace.getFacingDirection() == Direction.LEFT;
        if (left) employee.flipHorizontal(false);
    }

    @Override
    void leave() {
        employee.setAnimationState(Employee.AnimState.IDLE);
    }

    @Override
    void cancel() {

    }
}
