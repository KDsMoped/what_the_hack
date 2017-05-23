package de.hsd.hacking.Entities.Employees;

import de.hsd.hacking.Data.MovementProvider;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public abstract class EmployeeState{

    Employee employee;


    public EmployeeState(Employee employee){
        this.employee = employee;
    }

    abstract EmployeeState act(float deltaTime);

    public void enter(){
        employee.resetElapsedTime();
    }
    abstract void leave();
}
