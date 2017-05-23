package de.hsd.hacking.Data;

import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public interface MovementProvider {

    Vector2 getNextMovetoPoint(Employee employee);

    Vector2 getStartPosition(Employee employee);
}
