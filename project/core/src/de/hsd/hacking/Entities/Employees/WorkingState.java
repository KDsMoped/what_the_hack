package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Objects.Equipment.Computer;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class WorkingState extends EmployeeState {

    private Vector2 workingPosition;
    private Computer computer;
    private float timeBeforeIdle;
    private float elapsedTime = 0f;

    public WorkingState(Employee employee, Vector2 position, Computer computer) {
        super(employee);
        this.workingPosition = position;
        employee.setPosition(position.cpy());
        employee.getMovementProvider().getDiscreteTile(position).setEmployee(employee);
        this.computer = computer;
        this.timeBeforeIdle = MathUtils.random(10f, 30f);
    }

    @Override
    EmployeeState act(float deltaTime) {
        if (!isCanceled()){

            //TODO nur zu Debugzwecken, der State soll sich erst ändern wenn zB Mission fertig
            elapsedTime+= deltaTime;
            if (elapsedTime >= timeBeforeIdle){
                return new IdleState(employee);
            }
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
        computer.setOn(true);
    }

    @Override
    void leave() {
        employee.setAnimationState(Employee.AnimState.IDLE);
        computer.setOn(false);
        computer.deOccupy();
    }

    @Override
    public void cancel() {
        canceled = true;
    }
}
