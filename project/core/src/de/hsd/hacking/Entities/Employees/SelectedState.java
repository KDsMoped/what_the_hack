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
        if (!canceled){
            return new IdleState(employee);
        }
        return new IdleState(employee);
    }

    @Override
    void enter() {

    }

    @Override
    void leave() {

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
