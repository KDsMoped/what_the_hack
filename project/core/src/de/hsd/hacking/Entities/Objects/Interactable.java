package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

public interface Interactable {

    EmployeeState interact(Employee e);

    void occupy();
    void deOccupy();
    boolean isOccupied();
    boolean isDelegatingInteraction();

    Direction getFacingDirection();
}
