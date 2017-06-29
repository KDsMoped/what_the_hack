package de.hsd.hacking.Entities.Employees.States;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 14.06.2017.
 */

public class SelectedState extends EmployeeState {


    public SelectedState(Employee employee) {
        super(employee);
    }

    @Override
    public EmployeeState act(float deltaTime) {
        if (!canceled){
            return new IdleState(employee);
        }
        return new IdleState(employee);
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public void cancel() {
        employee.setSelected(false);
        if (employee.equals(employee.getStage().getSelectedEmployee())){
            employee.getStage().setSelectedEmployee(null);
        }
    }

    @Override
    public String getDisplayName() {
        return "Selected";
    }
}
