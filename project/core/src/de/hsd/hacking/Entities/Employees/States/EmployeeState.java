package de.hsd.hacking.Entities.Employees.States;

import de.hsd.hacking.Entities.Employees.Employee;

public abstract class EmployeeState{

    Employee employee;
    boolean canceled;


    public EmployeeState(Employee employee){
        this.employee = employee;
    }

    public abstract EmployeeState act(float deltaTime);

    /**
     * Gets called when an EmployeeState becomes active
     */
    public abstract void enter();

    /**
     * Gets called when an EmployeeState stops being active
     */
    public abstract void leave();

    /**
     * Gets the name of the state for UI displaying.
     * @return
     */
    public abstract String getDisplayName();

    public void cancel(){
        canceled = true;
    }
    boolean isCanceled(){
        return canceled;
    }
}
