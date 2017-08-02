package de.hsd.hacking.Entities.Employees.States;

import com.badlogic.gdx.Gdx;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Constants;

/**
 * This employee state lets the employee wait for a next action to be done.
 *
 * @author Hendrik
 */
public class WaitingState extends EmployeeState {

//    private boolean finished;
    private EmployeeState followingState;

    public WaitingState(Employee employee) {
        super(employee);
    }

    public void setFollowingState(EmployeeState state){
        followingState = state;
    }

    @Override
    public EmployeeState act(float deltaTime) {
        return followingState;
    }

    @Override
    public void enter() {
        employee.resetElapsedTime();
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Waiting state");
        employee.setAnimationState(Employee.AnimState.IDLE);
    }

    @Override
    public void leave() {

    }

    @Override
    public String getDisplayName() {
        return "Waiting";
    }
}
