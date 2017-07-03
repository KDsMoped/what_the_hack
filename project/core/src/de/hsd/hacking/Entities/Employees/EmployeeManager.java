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
    }

    /**
     * Refreshes the available employees in the job market.
     */
    public void refreshAvailableEmployees() {

        removeEmployees(REFRESH_RATE);
        populateAvailableEmployees();
    }

    private void removeEmployees(float fraction) {

        int removeAmount = MathUtils.clamp((int) (fraction * MAX_AVAILABLE_EMPLOYEES), 0, availableEmployees.size());

        for (int i = 0; i < removeAmount; i++) {
            availableEmployees.remove(0);
        }
    }

    public void populateAvailableEmployees(){

        int missing = MAX_AVAILABLE_EMPLOYEES - availableEmployees.size() + MathUtils.random(- AVAILABLE_EMPLOYEES_VARIANCE, AVAILABLE_EMPLOYEES_VARIANCE);

        availableEmployees.addAll(EmployeeFactory.CreateEmployees(missing, team.calcGameProgress()));
    }

    public void employ(Collection<Employee> employees) {
        for (Employee employee : employees) {
            employ(employee);
        }
    }

    public void employ(Employee employee) {
        if (hiredEmployees.contains(employee)) {
            Gdx.app.error(Constants.TAG, "Error: This employees is already hired!");
            return;
        }
        if (hiredEmployees.size() >= Constants.MAX_EMPLOYEE_COUNT) {
            Gdx.app.error(Constants.TAG, "Error: Exceeding max number of employees!");
            return;
        }

        availableEmployees.remove(employee);
        hiredEmployees.add(employee);
        employee.setTouchable(Touchable.enabled);
        GameStage.instance().addTouchable(employee);
    }

    public void dismiss(Employee employee) {

        if (employee == team.getSelectedEmployee()) team.deselectEmployee();
        employee.removeFromDrawingTile();
        employee.removeFromOccupyingTile();
        hiredEmployees.remove(employee);
        GameStage.instance().removeTouchable(employee);
    }

    public Collection<Employee> getAvailableEmployees() {
        return Collections.unmodifiableCollection(availableEmployees);
    }
    public Collection<Employee> getHiredEmployees() {
        return Collections.unmodifiableCollection(hiredEmployees);
    }

    public int getTeamSize(){return hiredEmployees.size();}
}
