package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EmployeeManager implements TimeChangedListener {

    private static final int MAX_AVAILABLE_EMPLOYEES = 16;
    private static final int AVAILABLE_EMPLOYEES_VARIANCE = 3;
    private static final float REFRESH_RATE = 0.15f;

    private static EmployeeManager instance;

    public static EmployeeManager instance() {

        if (instance == null) return new EmployeeManager();
        return instance;
    }

    private ArrayList<Employee> availableEmployees;
    private ArrayList<Employee> hiredEmployees;

    private ArrayList<Callback> refreshEmployeeListener = new ArrayList<Callback>();

    private Team team;
    private MessageManager messageManager;

    public EmployeeManager() {
        instance = this;

        availableEmployees = new ArrayList<Employee>();
        hiredEmployees = new ArrayList<Employee>();

        team = Team.instance();
        messageManager = MessageManager.instance();
        GameTime.instance.addTimeChangedListener(this);

        populateAvailableEmployees();
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

        if (missing > 0) availableEmployees.addAll(EmployeeFactory.createEmployees(missing, team.calcGameProgress()));
    }

    /**
     * Employs the entire collection of employees.
     *
     * @param employees
     */
    public void employ(Collection<Employee> employees) {
        for (Employee employee : employees) {
            employ(employee);
        }
    }

    /**
     * Employs this employee and adds it to the team.
     *
     * @param employee
     */
    public void employ(Employee employee) {
        if (hiredEmployees.contains(employee)) {
            Gdx.app.error(Constants.TAG, "Error: This employees is already hired!");
            return;
        }
        if (isTeamFull()) {
            Gdx.app.log(Constants.TAG, "Warning: Exceeding max number of employees!");
            return;
        }
        if (team.getMoney() < employee.getHiringCost()) {
            Gdx.app.log(Constants.TAG, "You have no money to hire this employee!");
            //TODO: Tell user about this.
            return;
        }

        team.reduceMoney(employee.getHiringCost());
        availableEmployees.remove(employee);
        hiredEmployees.add(employee);

//        GameStage.instance().addTouchable(employee);
        employee.onEmploy();
        notifyRefreshListeners();
    }

    /**
     * Dismisses the whole team.
     */
    public void dismissAll() {
        for (int i = hiredEmployees.size() - 1; i > 0; i--) {
            dismiss(hiredEmployees.get(i));
        }
    }

    /**
     * Removes this employee from the team.
     *
     * @param employee
     */
    public void dismiss(Employee employee) {

        if (employee == team.getSelectedEmployee()) team.deselectEmployee();
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
     * Returns a readonly List of all employees in the team.
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
        refreshAvailableEmployees();
        if(days % 7 == 6) messageManager.Info("Only one day until payday!");
    }

    @Override
    public void weekChanged(int week) {
        if (Constants.DEBUG) Gdx.app.log("", "Payday");

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
        if (team.getMoney() < employee.getSalary()) {
            if(Constants.DEBUG) Gdx.app.log(Constants.TAG, "You have no money to pay for your employees! " + employee.getName() + "leaves the team!");
            messageManager.Warning("You have no money to pay for your employees. " + employee.getName() + "leaves the team!");
            dismiss(employee);
            return;
        }

        team.reduceMoney(employee.getSalary());
    }

    public void addRefreshEmployeeListener(Callback callback) {
        if (!refreshEmployeeListener.contains(callback)) refreshEmployeeListener.add(callback);
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshEmployeeListener.toArray(new Callback[refreshEmployeeListener.size()])) {
            c.callback();
        }
    }
}
