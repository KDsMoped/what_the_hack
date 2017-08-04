package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.TeamManager;

public class Trump extends EmployeeSpecial {

    public Trump(Employee employee) {
        super(employee);
    }

    @Override
    public void onMissionCompleted(){
        TeamManager.instance().addMoney(TeamManager.instance().calcGameProgress() * 100);
    }

    /**
     * Returns the display name of this special as shown in UI.
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return "Covfefe";
    }

    /**
     * Returns the display description of this special as shown in UI.
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Despite the constant negative press covfefe.";
    }

    /**
     * Returns the balancing score cost of this special. Use negative values for negative specials and positive for positive specials.
     *
     * @return
     */
    @Override
    public float getScoreCost() {
        return 0;
    }
}
