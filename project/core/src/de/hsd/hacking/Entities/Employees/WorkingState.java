package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;

import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class WorkingState extends EmployeeState {


    public WorkingState(Employee employee) {
        super(employee);
    }

    @Override
    EmployeeState act(float deltaTime) {
        return null;
    }

    @Override
    void enter() {
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Working State");
    }

    @Override
    void leave() {

    }
}
