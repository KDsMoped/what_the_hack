package de.hsd.hacking.Entities.Employees;

import de.hsd.hacking.Data.MovementProvider;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public abstract class EmployeeState{

    Employee employee;
    boolean canceled;


    public EmployeeState(Employee employee){
        this.employee = employee;
    }

    abstract EmployeeState act(float deltaTime);

    /**
     * Gets called when an EmployeeState becomes active
     */
    abstract void enter();

    /**
     * Gets called when an EmployeeState stops being active
     */
    abstract void leave();

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
