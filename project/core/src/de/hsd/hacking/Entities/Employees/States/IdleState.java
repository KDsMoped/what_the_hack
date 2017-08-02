package de.hsd.hacking.Entities.Employees.States;

import com.badlogic.gdx.Gdx;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

public class IdleState extends EmployeeState {

    private float elapsedTime = 0f;
    private float stayTime;

    public IdleState(Employee employee) {
        super(employee);
        this.stayTime = RandomUtils.randomInt(11);

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
