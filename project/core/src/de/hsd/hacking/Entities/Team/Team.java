package de.hsd.hacking.Entities.Team;

import java.util.ArrayList;

import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Equipment.Computer;
import de.hsd.hacking.Entities.Equipment.Equipment;
import de.hsd.hacking.Entities.Equipment.EquipmentFactory;
import de.hsd.hacking.Entities.Team.Workspace;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import de.hsd.hacking.Stages.GameStage;

/**
 * Created by domin on 30.05.2017.
 * This class manages Emplyoees and Equipment in ArrayLists;
 */

public class Team {
    private final int maxEmployeeCount = 4;

    private String teamName;
    private static Stage stage;
//    private Group employees;
    private Group equipment;
    private Group workspaces;
    private static ArrayList<Employee> listOfEmployees;
    private static ArrayList<Equipment> listOfEquipment;
    private static ArrayList<Workspace> listOfWorkspaces;

    /* Resources */
    private int resource_Money;
    private int resource_Bandwidth;
    private int resource_ComputationPower;


    // Instanciation and Initialization of Team as a Singleton /////////////////////////////////////
    private static final Team instance = new Team();
    private Team(){}

    public static Team getInstance(){ return instance; }
    public static void initialize(Stage Stage) {
        stage = Stage;
        listOfEmployees = new ArrayList<Employee>();
        listOfEquipment = new ArrayList<Equipment>();
        listOfWorkspaces = new ArrayList<Workspace>();
    }


    /* Getting and Setting Team Name
     */
    public void setTeamName(String newTeamName) { teamName = newTeamName; }
    public String getTeamName() { return teamName; }


    // Manage Employee /////////////////////////////////////////////////////////////////////////////

    /* Creates a new Employee object and adds it to the team.
     * Returns: 0 for success, 1 when employeeCount exceeds maxEmployeeCount
     */
    public int createAndAddEmployee(Assets assets, Employee.EmployeeSkillLevel skillLevel,
                                    TileMap tileMap) {
        if(listOfEmployees.size() >= maxEmployeeCount) { return 1; }
        Employee e = new Employee(assets, skillLevel, tileMap, (GameStage)stage);
        listOfEmployees.add(e);
        e.setTouchable(Touchable.enabled);
        return 0;
    }

    /* Adds the given Employee oject to the team.
     * Returns: 0 for success, 1 when employeeCount exceeds maxEmployeeCount
     */
    public int addEmployee(Employee e) {
        if(listOfEmployees.size() >= maxEmployeeCount) { return 1; }
        listOfEmployees.add(e);
        return 0;
    }

    /* Returns the Employee object associated with the given index.
     */
    public Employee getEmployee(int index) { return listOfEmployees.get(index); }

    /* Returns the Group of Employees.
     */
//    public Group getEmployeeGroup() { return employees; }

    /* Returns the List of Employees.
     */
    public ArrayList<Employee> getEmployeeList() { return listOfEmployees; }

    /* Removes the Employee with the given Index from the Team.
     */
    public void removeEmployee(int index) {
        listOfEmployees.remove(index);
        Employee e = getEmployee(index);
//        employees.removeActor(e);
    }

    /* Removes the given Employee object from the Team.
     */
    public void removeEmployee(Employee e) {
        listOfEmployees.remove(e);
//        employees.removeActor(e);
    }

    /* Returns the current number of Employees in the Team.
     */
    public int getEmployeeCount() { return listOfEmployees.size(); }


    // Manage Equipment ////////////////////////////////////////////////////////////////////////////

    /* Create Equipment of the specified type and add it to the Team.
     */
    public void createAndAddEquipment(Equipment.EquipmentType type,
                                      Equipment.EquipmentAttributeLevel attributeLevel,
                                      float price,
                                      Assets assets) {
        Equipment equipment = EquipmentFactory.getEquipment(type, attributeLevel, price, assets);
        if(equipment != null) {
            listOfEquipment.add(equipment);
        }
    }

    /* Add given Equipment to the Team.
     */
    public void addEquipment(Equipment equipment) {
        listOfEquipment.add(equipment);
    }

    /* Returns the Equipment object associated with the given index.
     */
    public Equipment getEquipment(int index) { return listOfEquipment.get(index); }

    /* Removes the given Equipment from the list.
     */
    public void removeEquipment(Equipment e) {
        listOfEquipment.remove(e);
        //equipment.removeActor(e);
    }

    /* Removes the Equipment with the given index from the list.
     */
    public void removeEquipment(int index) {
        listOfEquipment.remove(index);
        Equipment e = getEquipment(index);
        //equipment.removeActor(e);
    }


    // Manage Workspaces ///////////////////////////////////////////////////////////////////////////

    /*
     *
     */
    public void addWorkspace() {
        Workspace w = new Workspace();
        listOfWorkspaces.add(w);
    }

    public void removeWorkspace(int index) { listOfWorkspaces.remove(index); }


    // Manage Resources ////////////////////////////////////////////////////////////////////////////

    /* Sets the bandwidth to the given value.
     */
    public void setMoney(int value) { resource_Money = value; }

    /* Returns the current money.
     */
    public int getMoney() { return resource_Money; }

    /* Raise the money by the given value.
     */
    public void addMoney(int value) { resource_Money += value; }

    /* Reduce the money by the given value.
     */
    public void reduceMoney(int value) { resource_Money -= value; }


    /* Sets the bandwidth to the given value.
     */
    public void setBandwith(int value) { resource_Bandwidth = value; }

    /* Returns the current bandwidth.
     */
    public int getBandwith() { return resource_Bandwidth; }

    /* Raise the bandwidth by the given value.
     */
    public void addBandwidth(int value) { resource_Bandwidth += value; }

    /* Reduce the bandwidth by the given value.
     */
    public void reduceBandwith(int value) { resource_Bandwidth -= value; }

    
    /* Sets the computation power to the given value.
     */
    public void setComputationPower(int value) { resource_ComputationPower = value; }

    /* Returns the current computation power
     */
    public int getComputationPower() { return resource_ComputationPower; }

    /* Raise the computation power by the given value.
     */
    public void addComputationPower(int value) { resource_ComputationPower += value; }

    /* Reduce the computation power by the given value.
     */
    public void reduceComputationPower(int value) { resource_ComputationPower -= value; }

}
