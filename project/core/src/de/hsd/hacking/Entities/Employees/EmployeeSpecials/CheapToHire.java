package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;

public class CheapToHire extends EmployeeSpecial {

    public CheapToHire(Employee employee) {
        super(employee);
    }

    @Override
    public String getDisplayName() {
        return "Cheap to Hire";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public float getScoreCost() {
        return 8;
    }

    @Override
    public float getHiringCostRelativeFactor() {
        return 0.7f;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean isLearnable() {
        return false;
    }
}
