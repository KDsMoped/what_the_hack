package de.hsd.hacking.Entities.Team;

import java.util.ArrayList;

import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Equipment.Equipment;

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
    ArrayList<Employee> listOfEmployees;
    ArrayList<Equipment> listOfEquipment;

    /* Resources */
    private int resource_Money;
    private int resource_Bandwith;
    private int resource_ComputationPower;

    /*
     *
     */
    public Team (Stage stage) {
        gameStage = stage;
        employees = new Group();
        gameStage.addActor(employees);

        listOfEmployees = new ArrayList<Employee>();
        listOfEquipment = new ArrayList<Equipment>();
    }

    /* Getting and Setting Team Name
     */
    public void setTeamName(String newTeamName) { teamName = newTeamName; }
    public String getTeamName() { return teamName; }

    // Employee Management

    /* Creates a new Employee object and adds it to the team.
     * Returns: 0 for success, 1 when employeeCount exceeds maxEmployeeCount
     */
    public int createAndAddEmployee(Assets assets, Employee.EmployeeSkillLevel skillLevel, MovementProvider movementProvider) {
        if(listOfEmployees.size() >= maxEmployeeCount) { return 1; }
        Employee e = new Employee(assets, skillLevel, movementProvider);
        listOfEmployees.add(e);
        employees.addActor(e);
        return 0;
    }

    /* Adds the given Employee oject to the team.
     * Returns: 0 for success, 1 when employeeCount exceeds maxEmployeeCount
     */
    public int addEmployee(Employee e) {
        if(listOfEmployees.size() >= maxEmployeeCount) { return 1; }
        listOfEmployees.add(e);
        employees.addActor(e);
        return 0;
    }

    /* Returns the Employee object associated with the given index.
     */
    public Employee getEmployee(int index) { return listOfEmployees.get(index); }

    /* Removes the associated Employee object from the Team.
     */
    public void removeEmployeeByIndex(int index) {
        listOfEmployees.remove(index);
        Employee e = getEmployee(index);
        employees.removeActor(e);
    }

    /* Removes the given Employee object from the Team.
     */
    public void removeEmployee(Employee e) {
        listOfEmployees.remove(e);
        employees.removeActor(e);
    }

    /* Returns the current number of Employees in the Team.
     */
    public int getEmployeeCount() { return listOfEmployees.size(); }


    // Equipment Management

    /*
     *
     */
    public void addEquipment() {

    }


    // Resources Management


}
