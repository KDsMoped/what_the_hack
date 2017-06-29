package de.hsd.hacking.Entities.Employees.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public class IdleState extends EmployeeState {

    private float elapsedTime = 0f;
    private float stayTime;

    public IdleState(Employee employee){
        super(employee);
        this.stayTime = MathUtils.random(5);

    }

    @Override
    public EmployeeState act(float deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= stayTime){
            return new MovingState(employee);
        }

        return null;
    }

    @Override
    public void enter() {
        employee.resetElapsedTime();
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Idle State");
        employee.setAnimationState(Employee.AnimState.IDLE);
    }

    @Override
    public void leave() {

    }

    @Override
    public String getDisplayName() {
        return "Unoccupied";
    }

    @Override
    public void cancel() {
        //nothing to do here;
    }
}
