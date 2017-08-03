package de.hsd.hacking.Utils.Callback;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Callback interface for various callbacks that send an {@link Employee}.
 *
 * @author Hendrik
 */
public interface EmployeeCallback {
    void callback(Employee employee);
}
