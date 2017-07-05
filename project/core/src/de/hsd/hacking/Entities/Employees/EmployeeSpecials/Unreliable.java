package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.MathUtilities;

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
    public void dayChanged(int days){
        if(MathUtils.random() > chance) return;

        //TODO: Implement user feedback
        Gdx.app.log(Constants.TAG, "An unreliable employee " + employee.getName() + " has left the team.");
        EmployeeManager.instance().dismiss(employee);
    }

    @Override
    public float getScoreCost() {

        return -14;
    }
}
