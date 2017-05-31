package de.hsd.hacking.Data;

import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public interface MovementProvider {

    /**
     * Returns a Vector2 position that an {@link Employee} can move to
     * @param employee the employee that should be moved
     * @return Vector2 position
     */
    Vector2 getNextMovetoPoint(Employee employee);

    /**
     * Returns a free Vector2 starting-position for an employee
     * @param employee
     * @return
     */
    Vector2 getStartPosition(Employee employee);
}
