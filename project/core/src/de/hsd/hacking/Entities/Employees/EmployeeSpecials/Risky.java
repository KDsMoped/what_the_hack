package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;

public class Risky extends EmployeeSpecial {

    private int level;

    /**
     * A risky employee has a higher chance for a critical success or failure roll. level = 0 means no effect.
     * @param employee
     * @param level
     */
    public Risky(Employee employee, int level) {
        super(employee);

        this.level = level;
    }

    @Override
    public int getCriticalFailureBonus() { return  level;}
    @Override
    public int getCriticalSuccessBonus() { return  level;}

    @Override
    public String getDisplayName() {

        switch (level){
            case 1:
                return "Risky";

            default:
                return "Risky " + level;

        }

    }

    @Override
    public String getDescription() {
        return "The 'all-or-nothing' way of life." ;
    }


    @Override
    public float getScoreCost() {

        return 0;
    }
}
