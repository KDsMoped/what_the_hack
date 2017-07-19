package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Utils.Constants;

public class Unreliable extends EmployeeSpecial {

    private float chance;


    public Unreliable(Employee employee, float chance) {
        super(employee);

        this.chance = chance;
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

        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "An unreliable employee " + employee.getName() + " has left the team.");
        MessageManager.instance().Warning("An unreliable employee " + employee.getName() + " has left the team.");

        EmployeeManager.instance().dismiss(employee);
    }

    @Override
    public float getScoreCost() {
        return -14;
    }
}
