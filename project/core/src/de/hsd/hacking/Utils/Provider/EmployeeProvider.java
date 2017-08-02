package de.hsd.hacking.Utils.Provider;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Used for references to an {@link Employee} that are evaluated when called. This should usually be implemented by an anonymous inner class.
 *
 * @author Hendrik
 */
public interface EmployeeProvider{
    Employee get();
}