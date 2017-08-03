package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Data.*;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The EmployeeManager holds lists of {@link Employee}s separated in available employees (the ones you can hire) and
 * hire employees (the ones in your teamManager).
 *
 * @author Hendrik
 */
public class EmployeeManager implements Manager, TimeChangedListener, ProtobufHandler {
    private Proto.EmployeeManager.Builder data;
    private static final int MAX_AVAILABLE_EMPLOYEES = 16;
    private static final int AVAILABLE_EMPLOYEES_VARIANCE = 3;
    private static final float REFRESH_RATE = 0.15f;

    private static EmployeeManager instance;

    /**
     * Creates an instance of this manager.
     */
    public static void createInstance() {
        if (instance != null){
            Gdx.app.error("", "ERROR: Instance of EmployeeManager has not been destroyed before creating new one.");
            return;
        }

        instance = new EmployeeManager();
    }

    /**
     * Provides an instance of this manager;
     * @return
     */
    public static EmployeeManager instance() {

        if (instance == null)
            Gdx.app.error("", "ERROR: Instance of EmployeeManager has not been created yet. Use createInstance() to do so.");

        return instance;
    }

    private ArrayList<Employee> availableEmployees;
    private ArrayList<Employee> hiredEmployees;

    private ArrayList<Callback> refreshEmployeeListener = new ArrayList<Callback>();

    private TeamManager teamManager;
    private MessageManager messageManager;

    public EmployeeManager() {
        availableEmployees = new ArrayList<Employee>();
        hiredEmployees = new ArrayList<Employee>();
    }

    /**
     * Refreshes the available employees in the job market.
     */
    public void refreshAvailableEmployees() {

        removeAvailableEmployees(REFRESH_RATE);
        populateAvailableEmployees();

        notifyRefreshListeners();
    }

    /**
     * Removes the given fraction of employees from the job market.
     *
     * @param fraction
     */
    private void removeAvailableEmployees(float fraction) {

        int removeAmount = MathUtils.clamp((int) (fraction * MAX_AVAILABLE_EMPLOYEES), 0, availableEmployees.size());

        for (int i = 0; i < removeAmount; i++) {
            availableEmployees.remove(0);
        }
    }

    /**
     * Adds as many new Employees to the available employees until the max number is reached.
     */
    public void populateAvailableEmployees() {

        int missing = MAX_AVAILABLE_EMPLOYEES - availableEmployees.size() + RandomUtils.randomIntWithin(-AVAILABLE_EMPLOYEES_VARIANCE, AVAILABLE_EMPLOYEES_VARIANCE);
        if (missing > 0) availableEmployees.addAll(EmployeeFactory.createEmployees(missing, teamManager.calcGameProgress()));
    }

    /**
     * Employs the entire collection of employees.
     *
     * @param employees
     */
    public void employ(Collection<Employee> employees, boolean pay) {
        for (Employee employee : employees) {
            employ(employee, pay);
        }
    }

    /**
     * Employs this employee and adds it to the teamManager.
     *
     * @param employee
     */
    public void employ(Employee employee, Boolean pay) {
        if (hiredEmployees.contains(employee)) {
            Gdx.app.error(Constants.TAG, "Error: This employees is already hired!");
            return;
        }
        if (isTeamFull()) {
            Gdx.app.log(Constants.TAG, "Warning: Exceeding max number of employees!");
            return;
        }

        if (pay) {
            if (teamManager.getMoney() < employee.getHiringCost()) {
                Gdx.app.log(Constants.TAG, "You have no money to hire this employee!");
                //TODO: Tell user about this.
                return;
            }

            teamManager.reduceMoney(employee.getHiringCost());
        }

        availableEmployees.remove(employee);
        hiredEmployees.add(employee);

//        GameStage.instance().addTouchable(employee);
        employee.onEmploy();
        notifyRefreshListeners();
    }

    /**
     * Dismisses the whole teamManager.
     */
    public void dismissAll() {
        for (int i = hiredEmployees.size() - 1; i > 0; i--) {
            dismiss(hiredEmployees.get(i));
        }
    }

    /**
     * Removes this employee from the teamManager.
     *
     * @param employee
     */
    public void dismiss(Employee employee) {

        if (employee == teamManager.getSelectedEmployee()) teamManager.deselectEmployee();
        employee.removeFromDrawingTile();
        employee.removeFromOccupyingTile();
        hiredEmployees.remove(employee);
        GameStage.instance().removeTouchable(employee);
        employee.onDismiss();

        notifyRefreshListeners();
    }

    /**
     * Returns a readonly List of all available employees on the job market.
     *
     * @return
     */
    public Collection<Employee> getAvailableEmployees() {
        return Collections.unmodifiableCollection(availableEmployees);
    }

    /**
     * Returns a readonly List of all employees in the teamManager.
     *
     * @return
     */
    public Collection<Employee> getHiredEmployees() {
        return Collections.unmodifiableCollection(hiredEmployees);
    }

    public int getTeamSize() {
        return hiredEmployees.size();
    }

    public boolean isTeamFull() {
        return hiredEmployees.size() >= Constants.MAX_EMPLOYEE_COUNT;
    }

    @Override
    public void timeChanged(float time) {
    }

    @Override
    public void timeStepChanged(int step) {
    }

    @Override
    public void dayChanged(int days) {
        if (RandomUtils.randomIntWithin(0, 9) < 5) {
            refreshAvailableEmployees();
        }
        if (days % 7 == 6) messageManager.Info("Only one day until payday!");
    }

    @Override
    public void weekChanged(int week) {
        payday();
    }

    /**
     * Pays all employees.
     */
    private void payday() {
        messageManager.Info("Payday!");

        for (Employee employee : hiredEmployees.toArray(new Employee[hiredEmployees.size()])) {
            pay(employee);
        }
    }

    /**
     * Pays the given employee.
     *
     * @param employee
     */
    private void pay(Employee employee) {
        int salary = employee.getSalary();

        if (teamManager.getMoney() < salary) {
            if (Constants.DEBUG)
                Gdx.app.log(Constants.TAG, "You have no money to pay for your employees! " + employee.getName() + " leaves the teamManager!");
            messageManager.Warning("You have no money to pay for your employees. " + employee.getName() + " leaves the teamManager!");
            dismiss(employee);
            return;
        }

        teamManager.reduceMoney(salary);
    }

    public void addRefreshEmployeeListener(Callback callback) {
        if (!refreshEmployeeListener.contains(callback)) refreshEmployeeListener.add(callback);
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshEmployeeListener.toArray(new Callback[refreshEmployeeListener.size()])) {
            c.callback();
        }
    }

    public static void setInstance(EmployeeManager instance) {
        EmployeeManager.instance = instance;
    }

    /**
     * Save all the current missions
     * @return Build Proto MissionManager object to write to disk.
     */
    public Proto.EmployeeManager Save() {
        Proto.EmployeeManager.Builder builder = Proto.EmployeeManager.newBuilder();

        for (Employee employee: hiredEmployees) {
            builder.addHiredEmployees(employee.getData());
        }

        for (Employee employee: availableEmployees) {
            builder.addAvailableEmployees(employee.getData());
        }

        return builder.build();
    }

    public int getHiredEmployeeId(Employee employee) {
        return hiredEmployees.indexOf(employee);
    }

    public Employee getHiredEmployee(int id) {
        return hiredEmployees.get(id);
    }

    /**
     * Restores the missions from a previous game.
     * @return True if missions where loaded.
     */
    public Boolean Load() {
        Proto.EmployeeManager.Builder proto = SaveGameManager.getEmployeeManager();
        if (proto != null) {
            for (Proto.Employee employee : proto.getHiredEmployeesList()) {
                Employee e = new Employee(employee.toBuilder());
                availableEmployees.add(e);
                employ(e, false);
            }

            for (Proto.Employee employee : proto.getAvailableEmployeesList()) {
                availableEmployees.add(new Employee(employee.toBuilder()));
            }

            return true;
        }
        return false;
    }

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    @Override
    public void initReferences() {
        GameTime.instance().addTimeChangedListener(this);

        teamManager = TeamManager.instance();
        messageManager = MessageManager.instance();
    }

    /**
     * Creates the default state of this manager when a new game is started.
     */
    @Override
    public void loadDefaultState() {
        populateAvailableEmployees();
        employ(EmployeeFactory.createEmployees(Constants.STARTING_TEAM_SIZE), false);
    }

    /**
     * Recreates the state this manager had before serialization.
     */
    @Override
    public void loadState() {

    }

    /**
     * Destroys manager this instance.
     */
    @Override
    public void cleanUp() {
        instance = null;
    }
}
