package de.hsd.hacking.Entities.Employees;

/**
 * Created by Cuddl3s on 14.06.2017.
 */

public class SelectedState extends EmployeeState {


    public SelectedState(Employee employee) {
        super(employee);
    }

    @Override
    EmployeeState act(float deltaTime) {
        return null;
    }

    @Override
    void enter() {

    }

    @Override
    void leave() {

    }
}
