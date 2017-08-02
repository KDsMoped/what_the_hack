package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * This employee special improves employees chance for a critical success roll.
 *
 * @author Hendrik
 */
public class LuckyDevil extends EmployeeSpecial {

    /**
     * A Lucky Devil has a higher chance for a critical success roll.
     * @param employee
     */
    public LuckyDevil(Employee employee) {
        super(employee);
    }

    @Override
    public int getCriticalSuccessBonus() { return  1;}

    @Override
    public String getDisplayName() {

       return "Lucky Devil";
    }

    @Override
    public String getDescription() {
        return "Wins every game of poker.";
    }


    @Override
    public float getScoreCost() {

        return 6;
    }
}
