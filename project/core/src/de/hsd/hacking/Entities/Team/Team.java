package de.hsd.hacking.Entities.Team;import java.util.ArrayList;import com.badlogic.gdx.Gdx;import de.hsd.hacking.Entities.Employees.Employee;import de.hsd.hacking.Entities.Employees.EmployeeFactory;import de.hsd.hacking.Entities.Objects.Equipment.Equipment;import de.hsd.hacking.Entities.Objects.Equipment.EquipmentFactory;import com.badlogic.gdx.scenes.scene2d.Group;import com.badlogic.gdx.scenes.scene2d.Touchable;import de.hsd.hacking.Stages.GameStage;import de.hsd.hacking.Utils.Constants;/** * Created by domin on 30.05.2017. * This class manages Emplyoees and Equipment in ArrayLists; */public class Team {    private String teamName;    private GameStage stage;    //    private Group employees;    private Group equipment;//    private Group workspaces;    private ArrayList<Employee> listOfEmployees;    private ArrayList<Equipment> listOfEquipment;    private ArrayList<Workspace> listOfWorkspaces;    private Employee selectedEmployee;    /* Resources */    private int resource_Money = Constants.STARTING_MONEY;    private int resource_Bandwidth = 0;    private int resource_ComputationPower = 0;    // Instanciation and Initialization of Team as a Singleton /////////////////////////////////////    private static final Team instance = new Team();    private Team() {        listOfEmployees = new ArrayList<Employee>();        listOfEquipment = new ArrayList<Equipment>();        listOfWorkspaces = new ArrayList<Workspace>();    }    public static Team instance() {        return instance;    }    public void initialize(GameStage Stage) {        stage = Stage;        addEmployees(EmployeeFactory.createEmployees(Constants.STARTING_TEAM_SIZE));    }    /* Getting and Setting Team Name     */    public void setTeamName(String newTeamName) {        teamName = newTeamName;    }    public String getTeamName() {        return teamName;    }    // Manage Employees ////////////////////////////////////////////////////////////////////////////    /* Creates a new Employee object and adds it to the team.     * Returns: 0 for success, 1 when employeeCount exceeds MAX_EMPLOYEE_COUNT     */    public int createAndAddEmployee(Employee.EmployeeSkillLevel skillLevel) {        if (listOfEmployees.size() >= Constants.MAX_EMPLOYEE_COUNT) {            return 1;        }        addEmployee(new Employee(skillLevel));        return 0;    }    public void addEmployees(ArrayList<Employee> employees) {        for (Employee e : employees) {            addEmployee(e);        }    }    /* Adds the given Employee object to the team.     */    public void addEmployee(Employee e) {        if (listOfEmployees.contains(e)) {            Gdx.app.error(Constants.TAG, "Error: This employees is already hired!");            return;        }        if (listOfEmployees.size() >= Constants.MAX_EMPLOYEE_COUNT) {            Gdx.app.error(Constants.TAG, "Error: Exceeding max number of employees!");            return;        }        e.setTouchable(Touchable.enabled);        listOfEmployees.add(e);    }    /* Returns the Employee object associated with the given index.     */    public Employee getEmployee(int index) {        return listOfEmployees.get(index);    }    /* Returns the List of Employees.     */    public ArrayList<Employee> getEmployeeList() {        return listOfEmployees;    }    /* Removes the Employee with the given Index from the Team.     */    public void removeEmployee(int index) {        removeEmployee(listOfEmployees.get(index));    }    /* Removes the given Employee object from the Team.     */    public void removeEmployee(Employee e) {        if (e == selectedEmployee) deselectEmployee();        e.removeFromDrawingTile();        e.removeFromOccupyingTile();        listOfEmployees.remove(e);        stage.removeTouchable(e);    }    /* Returns the current number of Employees in the Team.     */    public int getEmployeeCount() {        return listOfEmployees.size();    }    // Manage Equipment ////////////////////////////////////////////////////////////////////////////    /* Create Equipment of the specified type and add it to the Team.     */    public void createAndAddEquipment(Equipment.EquipmentType type) {        Equipment equipment = EquipmentFactory.getEquipment(type);        if (equipment != null) {            listOfEquipment.add(equipment);        }    }    /* Add given Equipment to the Team.     */    public void addEquipment(Equipment equipment) {        listOfEquipment.add(equipment);        addResource(equipment.getAttributeType(), equipment.getAttributeValue());    }    /* Returns the Equipment object associated with the given index.     */    public Equipment getEquipment(int index) {        return listOfEquipment.get(index);    }    /* Removes the given Equipment from the list.     */    public void removeEquipment(Equipment e) {        listOfEquipment.remove(e);        reduceResource(e.getAttributeType(), e.getAttributeValue());        //equipment.removeActor(e);    }    /* Removes the Equipment with the given index from the list.     */    public void removeEquipment(int index) {        listOfEquipment.remove(index);        Equipment e = getEquipment(index);        reduceResource(e.getAttributeType(), e.getAttributeValue());        //equipment.removeActor(e);    }    public ArrayList<Equipment> getEquipmentList() {        return listOfEquipment;    }    // Manage Workspaces ///////////////////////////////////////////////////////////////////////////    /*     *     */    public void addWorkspace() {        Workspace w = new Workspace();        listOfWorkspaces.add(w);    }    public int getWorkspaceCount() {        return listOfWorkspaces.size();    }    public void removeWorkspace(int index) {        listOfWorkspaces.remove(index);    }    // Manage Resources ////////////////////////////////////////////////////////////////////////////    /* Sets the bandwidth to the given value.     */    public void setMoney(int value) {        resource_Money = value;    }    /* Returns the current money.     */    public int getMoney() {        return resource_Money;    }    /* Raise the money by the given value.     */    public void addMoney(int value) {        resource_Money += value;    }    /* Reduce the money by the given value.     */    public void reduceMoney(int value) {        resource_Money -= value;    }    /* Sets the bandwidth to the given value.     */    public void setBandwith(int value) {        resource_Bandwidth = value;    }    /* Returns the current bandwidth.     */    public int getBandwith() {        return resource_Bandwidth;    }    /* Raise the bandwidth by the given value.     */    public void addBandwidth(int value) {        resource_Bandwidth += value;    }    /* Reduce the bandwidth by the given value.     */    public void reduceBandwidth(int value) {        resource_Bandwidth -= value;    }    /* Sets the computation power to the given value.     */    public void setComputationPower(int value) {        resource_ComputationPower = value;    }    /* Returns the current computation power     */    public int getComputationPower() {        return resource_ComputationPower;    }    /* Raise the computation power by the given value.     */    public void addComputationPower(int value) {        resource_ComputationPower += value;    }    /* Reduce the computation power by the given value.     */    public void reduceComputationPower(int value) {        resource_ComputationPower -= value;    }    public Employee getSelectedEmployee() {        return selectedEmployee;    }    public void setSelectedEmployee(Employee selectedEmployee) {        deselectEmployee();        this.selectedEmployee = selectedEmployee;    }    public boolean isEmployeeSelected() {        return selectedEmployee != null;    }    public void deselectEmployee() {        if (selectedEmployee != null) {            selectedEmployee.setSelected(false);            selectedEmployee = null;        }    }    public int calcGameProgress() {        float result = 1;        result += resource_Money * 0.00001f;        result += resource_Bandwidth * 0.005f;        result += resource_ComputationPower * 0.003f;        return (int) result;    }    public void addResource(Equipment.EquipmentAttributeType type, int value) {        switch(type) {            case BANDWIDTH:                addBandwidth(value);            case MONEY:                addMoney(value);            case COMPUTATIONPOWER:                addComputationPower(value);        }    }    public void reduceResource(Equipment.EquipmentAttributeType type, int value) {        switch(type) {            case BANDWIDTH:                reduceBandwidth(value);            case MONEY:                reduceMoney(value);            case COMPUTATIONPOWER:                reduceComputationPower(value);        }    }}