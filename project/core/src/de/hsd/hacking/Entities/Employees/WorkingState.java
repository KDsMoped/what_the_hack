package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class WorkingState extends EmployeeState {


    public WorkingState(Employee employee, Vector2 position) {
        super(employee);
        employee.setPosition(position);
        employee.getMovementProvider().getTile(position).setEmployee(employee);
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
    }

    @Override
    void leave() {
        employee.setAnimationState(Employee.AnimState.IDLE);
    }

    @Override
    void cancel() {

    }
}
