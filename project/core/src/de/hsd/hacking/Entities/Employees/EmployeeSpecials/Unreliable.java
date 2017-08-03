package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Utils.Constants;

/**
 * This employee special makes an employee leave the teamManager randomly at a small chance.
 *
 * @author Hendrik
 */
public class Unreliable extends EmployeeSpecial {

    private static final float chance = 0.02f;


    public Unreliable(Employee employee) {
        super(employee);

//        this.chance = chance;
    }

    @Override
    public String getDisplayName() {
        return "Unreliable";
    }

    @Override
    public String getDescription() {

        return "May leave the company any day.";
    }
    public void dayChanged(int days) {
        if (MathUtils.random() > chance) return;

        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "An unreliable employee " + employee.getName() + " has left the teamManager.");
        MessageManager.instance().Warning("An unreliable employee " + employee.getName() + " has left the teamManager.");

        EmployeeManager.instance().dismiss(employee);
    }

    @Override
    public float getScoreCost() {
        return -14;
    }
}
