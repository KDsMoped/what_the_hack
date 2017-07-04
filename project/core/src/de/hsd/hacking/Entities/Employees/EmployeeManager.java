package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EmployeeManager {

    private static final int MAX_AVAILABLE_EMPLOYEES = 16;
    private static final int AVAILABLE_EMPLOYEES_VARIANCE = 3;
    private static final float REFRESH_RATE = 0.3f;

    private static EmployeeManager instance;

    public static EmployeeManager instance() {

        if (instance == null) return new EmployeeManager();
        return instance;
    }

    private ArrayList<Employee> availableEmployees;
    private ArrayList<Employee> hiredEmployees;

    private Team team;

    public EmployeeManager() {
        instance = this;

        availableEmployees = new ArrayList<Employee>();
        hiredEmployees = new ArrayList<Employee>();

        team = Team.instance();
        populateAvailableEmployees();
    }

    /**
     * Refreshes the available employees in the job market.
     */
    public void refreshAvailableEmployees() {

        removeAvailableEmployees(REFRESH_RATE);
        populateAvailableEmployees();
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

        int missing = MAX_AVAILABLE_EMPLOYEES - availableEmployees.size() + MathUtils.random(-AVAILABLE_EMPLOYEES_VARIANCE, AVAILABLE_EMPLOYEES_VARIANCE);

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
     * Employs this employee and adds it to the team. Returns 1 if successful.
     *
     * @param employee
     * @return
     */
    public int employ(Employee employee) {
        if (hiredEmployees.contains(employee)) {
            Gdx.app.error(Constants.TAG, "Error: This employees is already hired!");
            return 0;
        }
        if (isTeamFull()) {
            Gdx.app.log(Constants.TAG, "Warning: Exceeding max number of employees!");
            return 0;
        }
        if (team.getMoney() < employee.getHiringCost()) {
            Gdx.app.log(Constants.TAG, "You have no money to hire this employee!");
            //TODO: Tell user about this.
            return 0;
        }

        team.reduceMoney(employee.getHiringCost());
        availableEmployees.remove(employee);
        hiredEmployees.add(employee);
        employee.setTouchable(Touchable.enabled);
        GameStage.instance().addTouchable(employee);
        employee.employ();
        return 1;
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

    public boolean isTeamFull(){
        return hiredEmployees.size() >= Constants.MAX_EMPLOYEE_COUNT;
    }
}
