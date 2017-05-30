package de.hsd.hacking.Entities.Team;

import java.util.ArrayList;
import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by domin on 30.05.2017.
 */

public class Team {
    private String teamName;
    ArrayList<Employee> listOfEmployees = new ArrayList<Employee>();

    public void setTeamName(String newTeamName) { teamName = newTeamName; }
    public String getTeamName() { return teamName; }

    public void addEmployee(Employee e) {
        listOfEmployees.add(e);
    }

    public void addEquipment() {

    }


}
