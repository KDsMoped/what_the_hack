package de.hsd.hacking.Entities.Team;

import java.util.ArrayList;

import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Assets.Assets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by domin on 30.05.2017.
 * This class manages Emplyoees and Equipment in ArrayLists;
 */

public class Team {
    private final int maxEmployeeCount = 4;

    private String teamName;
    private Stage gameStage;
    private Group employees;

    ArrayList<Employee> listOfEmployees = new ArrayList<Employee>();

    /*
     *
     */
    public Team (Stage stage) {
        gameStage = stage;
        employees = new Group();
        gameStage.addActor(employees);
    }

    /*
     *
     * Returns: 0 for success, 1 when employeeCount exceeds maxEmployeeCount
     */
    public int addEmployee(Assets assets, Employee.EmployeeSkillLevel skillLevel, MovementProvider movementProvider) {
        if(listOfEmployees.size() >= maxEmployeeCount) { return 1; }
        Employee e = new Employee(assets, skillLevel, movementProvider);
        listOfEmployees.add(e);
        employees.addActor(e);
        return 0;
    }

    public Employee getEmployee (int index) { return listOfEmployees.get(index); }
    public void removeEmployee (int index) { listOfEmployees.remove(index); }
    public int getEmployeeCount() { return listOfEmployees.size(); }

    public void addEquipment() {

    }

    public void setTeamName(String newTeamName) { teamName = newTeamName; }
    public String getTeamName() { return teamName; }
}
