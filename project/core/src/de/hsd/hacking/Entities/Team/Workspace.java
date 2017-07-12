package de.hsd.hacking.Entities.Team;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;

/**
 * Created by domin on 07.06.2017.
 *
 * A Workspace holds one Employee and one Computer.
 */

public class Workspace {

    private Employee employee;
    private Computer computer;


    Workspace(Employee employee, Computer computer) {
        this.employee = employee;
        this.computer = computer;
    }

    Workspace(Employee employee) {
        this.employee = employee;
    }

    Workspace(Computer computer) {
        this.computer = computer;
    }

    Workspace() {}


    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Computer getComputer() { return computer; }
    public void setComputer(Computer computer) { this.computer = computer; }

}
