package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;

public class FastLearner extends EmployeeSpecial {

    public FastLearner(Employee employee) {
        super(employee);
    }

    @Override
    public String getDisplayName() {
        return "Fast Learner";
    }

    @Override
    public String getDescription() {
        return "Quickly gets into things.";
    }

    @Override
    public float getScoreCost() {
        return 7;
    }

    @Override
    public void onMissionCompleted(){
        employee.incrementFreeScore(0.5f + employee.getCurrentMission().getDifficulty() * 0.25f);
    }
}
