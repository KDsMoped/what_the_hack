package de.hsd.hacking.Entities.Employees.States;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Represents an employees current occupation. (State pattern).
 * @author Florian
 */
public abstract class EmployeeState {

    Employee employee;
    boolean canceled;


    public EmployeeState(final Employee employee1){
        this.employee = employee1;
    }

    public abstract EmployeeState act(float deltaTime);

    /**
     * Gets called when an EmployeeState becomes active.
     */
    public abstract void enter();

    /**
     * Gets called when an EmployeeState stops being active.
     */
    public abstract void leave();

    /**
     * Gets the name of the state for UI displaying.
     * @return String representation of current state.
     */
    public abstract String getDisplayName();

    public void cancel(){
        canceled = true;
    }
    boolean isCanceled(){
        return canceled;
    }
}
